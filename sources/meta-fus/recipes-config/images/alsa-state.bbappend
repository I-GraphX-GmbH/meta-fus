#FuS alsa-state should be used

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://asound.state"

do_install_append() {
    install -d ${D}/var/lib/alsa

    install -m 0644 ${WORKDIR}/asound.state   ${D}/var/lib/alsa/
}