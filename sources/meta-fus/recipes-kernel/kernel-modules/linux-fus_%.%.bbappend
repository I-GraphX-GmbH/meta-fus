DESCRIPTION = "Remove kernel modules from list KERNEL_OMIT_MODULES"

kmoddir = "/lib/modules/${KERNEL_VERSION}"
INSANE_SKIP_${PN} = "installed-vs-shipped"

do_install_append() {
	if [ ! -z "${KERNEL_OMIT_MODULES}" ]; then
		cd ${D}${kmoddir}
		rm ${KERNEL_OMIT_MODULES}
	fi
}
