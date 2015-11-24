package com.aspire.dubsmash.util

/**
 * Created by sia on 11/18/15.
 */

enum class Font(val resourceId: String) {
    NAZANIN_BOLD("b_nazanin_bold.ttf"), AFSANEH("a_afsaneh.ttf"), SANS("a_iranian_sans.ttf"),
    MASHIN_TAHRIR("a_mashin_tahrir.ttf"), NASKH("a_naskh_tahrir.ttf"), NEGAR("a_negar.ttf"),
    FANTECY("fantecy.ttf"), DAST_NEVESHTE("b_kamran_bold.ttf"), KOODAK("b_koodak.ttf"),
    SETAREH("b_setareh_bold.ttf"), DROID_ARABIK("droid_arabic_naskh.ttf"), URDU("urdu.ttf"),
    IRAN_NASTALIQ("Iran_nastaliq.ttf")
}

enum class FontWeight(name: String) {
    BOLD("Bold"), REGULAR("Regular")
}

enum class FragmentId {
    ADD_SOUND, CUT_SOUND, FIRST_RUN, MY_SOUNDS, MY_DUBS, PROCESS_DUB,
    RECORD_SOUND, SEND_SOUND, SOUNDS, DUBS, VIEW_PAGER, RECORD_DUB
}