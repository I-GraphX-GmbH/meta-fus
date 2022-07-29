# Copyright (C) 2020 F&S Elektronik Systeme GmbH
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "bootloader for F&S boards and modules"
require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"
DEPENDS:append = " python dtc-native bison-native"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "file://u-boot-2020.04-fus.tar.bz2"
S = "${WORKDIR}/u-boot-2020.04-fus"

UBOOT_MAKE_TARGET = "all"
COMPATIBLE_MACHINE = "(mx8)"
UBOOT_NAME_mx8 = "u-boot-${MACHINE}-${UBOOT_CONFIG}.bin"

BOOT_TOOLS = "imx-boot-tools"


# FIXME: Allow setting boardconfig by environment variables.
#        The environment variables have to be added to the BB_ENV_EXTRAWHITE e.g.:
#        export BB_ENV_EXTRAWHITE=" $BB_ENV_EXTRAWHITE CONFIG_FUS_BOARDTYPE CONFIG_FUS_BOARDREV CONFIG_FUS_FEATURES2"
do_compile:prepend () {
	export CONFIG_FUS_BOARDTYPE=${CONFIG_FUS_BOARDTYPE};
	export CONFIG_FUS_BOARDREV=${CONFIG_FUS_BOARDREV};
	export CONFIG_FUS_FEATURES2=${CONFIG_FUS_FEATURES2};
}

do_deploy:append:mx8m () {
    # Deploy the mkimage, u-boot-nodtb.bin and picocoremx8mp.dtb for mkimage to generate boot binary
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
                    install -m 0777 ${B}/${config}/arch/arm/dts/${UBOOT_DTB_NAME}  ${DEPLOYDIR}/${BOOT_TOOLS}
                    install -m 0777 ${B}/${config}/u-boot-nodtb.bin  ${DEPLOYDIR}/${BOOT_TOOLS}/u-boot-nodtb.bin-${MACHINE}-${UBOOT_CONFIG}
                fi
            done
            unset  j
        done
        unset  i
    fi

}

PACKAGE_ARCH = "${MACHINE_ARCH}"
