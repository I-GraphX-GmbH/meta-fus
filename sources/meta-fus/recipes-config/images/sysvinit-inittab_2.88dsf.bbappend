#FuS inittab must be used

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://inittab"

do_install_append() {
    install -d ${D}${sysconfdir}

    install -m 0644 ${WORKDIR}/inittab   ${D}${sysconfdir}/
}