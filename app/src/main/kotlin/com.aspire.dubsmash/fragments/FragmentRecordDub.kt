package com.aspire.dubsmash.fragments

import android.app.Fragment
import android.content.pm.PackageManager
import android.hardware.Camera
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.ToggleButton
import com.aspire.dubsmash.R
import com.aspire.dubsmash.activities.ActivityMain
import com.aspire.dubsmash.util.*
import org.jetbrains.anko.act
import org.jetbrains.anko.ctx
import org.jetbrains.anko.onClick
import org.jetbrains.anko.onUiThread

/**
 * Created by sia on 11/24/15.
 */
class FragmentRecordDub : Fragment(), SurfaceHolder.Callback {
    private val surfaceView: SurfaceView by bindView(R.id.surface_view)
    private val seekBar: SeekBar by bindView(R.id.seek_bar)
    private val toggle: ToggleButton by bindView(R.id.toggle_recording)
    private val switchCamera: ImageButton by bindView(R.id.switch_camera)

    private val mediaRecorder = MediaRecorder()
    private val mediaPlayer: MediaPlayer by lazy { MediaPlayer.create(ctx, Uri.parse(soundPath)) }
    private val surfaceHolder: SurfaceHolder by lazy { setUpHolder() }

    private var recording: Boolean = false
    private var cameraId: CameraId = CameraId.FRONT
    private lateinit var camera: Camera
    private lateinit var preferredVideoSize: Camera.Size
    private lateinit var preferredPreviewSize: Camera.Size
    private lateinit var soundPath: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_record_dub, container, false)
        if (act is ActivityMain) setUpToolbar()
        soundPath = arguments.getString(SOUND_PATH)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
        changeCameraFrontVisibility()
    }

    private fun setUpToolbar() {
        val toolbar = (act as ActivityMain).toolbar
        toolbar.setTitle("ضبط داب جدید")
    }

    private fun setUpViewsStyle() {
        seekBar.progress = 0
        toggle.isChecked = false
    }

    private fun isCameraFrontAvailable(): Boolean {
        return act.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
    }

    private fun changeCameraFrontVisibility() {
        if (!isCameraFrontAvailable()) {
            switchCamera.visibility = View.GONE
            cameraId = CameraId.BACK
        }
    }

    private fun setListeners() {
        switchCamera.onClick {
            if (!recording) switchCamera()
        }

        toggle.onClick {
            if ((it as ToggleButton).isChecked) {
                startRecording()
            } else {
                cancelRecording()
            }
        }
    }

    private fun setUpHolder(): SurfaceHolder {
        val holder = surfaceView.holder
        holder.addCallback(this)
        return holder
    }

    private fun startRecording() {
        recording = true
        mediaPlayer.start()
        mediaRecorder.setUp()
        mediaRecorder.start()
        executor.execute {
            Thread.sleep(mediaPlayer.duration.toLong())
        }
    }

    private fun cancelRecording() {
        mediaPlayer.stopAndRelease()
        mediaRecorder.stopAndReset()
        releaseCamera()
    }

    private fun startProgress() {
        seekBar.max = mediaPlayer.duration
        executor.execute {
            while (mediaPlayer.isPlaying) {
                onUiThread { seekBar.progress = mediaPlayer.currentPosition }
                Thread.sleep(250)
            }
        }
    }

    private fun MediaRecorder.stopAndRelease() {
        stopAndReset()
        release()
    }

    private fun MediaRecorder.stopAndReset() {
        if (recording) stop()
        reset()
        recording = false
    }

    private fun releaseCamera() {
        camera.stopPreview()
        camera.release()
    }

    private fun startCamera() {
        camera = Camera.open(cameraId.id)
        camera.setDisplayOrientation(90)
        val parameters = camera.parameters
        preferredVideoSize = parameters.getPreferredVideoSize()
        preferredPreviewSize = parameters.getPreferredPreviewSize()
        parameters.setPreviewSize(preferredPreviewSize.width, preferredPreviewSize.height)
        camera.parameters = parameters
        camera.setPreviewDisplay(surfaceHolder)
        camera.startPreview()
    }

    private fun MediaRecorder.setUp() {
        setCamera(camera)
        setVideoSource(MediaRecorder.VideoSource.DEFAULT)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        setVideoEncodingBitRate(3000000)
        setVideoFrameRate(30)
        setVideoSize(preferredVideoSize.width, preferredVideoSize.height)
        setOrientationBasedOnCamera()
        setOutputFile(tempVideoPath)
        setPreviewDisplay(surfaceHolder.surface)
        prepare()
    }

    private fun switchCamera() {
        releaseCamera()
        mediaRecorder.stopAndReset()
        if (cameraId == CameraId.FRONT) cameraId = CameraId.BACK
        else cameraId = CameraId.FRONT
        startCamera()
    }

    private fun setOrientationBasedOnCamera() {
        if (cameraId == CameraId.FRONT) mediaRecorder.setOrientationHint(270)
        else mediaRecorder.setOrientationHint(90)
    }

    private fun Camera.Parameters.getPreferredVideoSize(): Camera.Size {
        var supportedVideoSizes = supportedVideoSizes
        if (supportedVideoSizes != null) return supportedVideoSizes.getPreferredSize()
        else return getPreferredPreviewSize()
    }

    private fun Camera.Parameters.getPreferredPreviewSize(): Camera.Size {
        val supportedPreviewSizes = supportedPreviewSizes
        return supportedPreviewSizes.getPreferredSize()
    }

    private fun List<Camera.Size>.getPreferredSize(): Camera.Size {
        val ratios = FloatArray(this.size)
        for (i in this.indices) {
            val width = this[i].width.toFloat()
            val height = this[i].height.toFloat()
            ratios[i] = if (width > height) width / height else height / width
        }
        return this[ratios.minElementIndex()!!]
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        cancelRecording()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
    }

    override fun onResume() {
        super.onResume()
        setUpViewsStyle()
        mediaPlayer.reset()
        startCamera()
    }

    override fun onPause() {
        super.onPause()
        if (recording) mediaRecorder.stopAndReset()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (recording) mediaRecorder.stopAndRelease()
    }
}