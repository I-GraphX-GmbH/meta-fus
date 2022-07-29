SUMMARY = "F&S mini-x-session"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://mini-x-session"

do_install:append() {
	install -m 0755 ${WORKDIR}/mini-x-session ${D}/${bindir}
}
