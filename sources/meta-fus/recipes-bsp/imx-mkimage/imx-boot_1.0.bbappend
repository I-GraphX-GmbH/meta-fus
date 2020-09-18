# Copyright 2020 F&S Elektronik Systeme GmbH

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI[md5sum] = "ce864e0f149993d41e9480bf4c2e8510"
SRC_URI += "file://0001-Add-option-arg-for-nand-device.patch"
SRC_URI[md5sum] = "b8282a2b78c1f40043f50e9333e07769"
SRC_URI += "file://0002-Correct-dtbs-env-to-use-different-DTs.patch"

do_compile() {
    compile_${SOC_FAMILY}
    # Copy TEE binary to SoC target folder to mkimage
    if ${DEPLOY_OPTEE}; then
        cp ${DEPLOY_DIR_IMAGE}/tee.bin                       ${BOOT_STAGING}
    fi
    # mkimage for i.MX8
    for target in ${IMXBOOT_TARGETS}; do
        if [ "$target" = "flash_linux_m4_no_v2x" ]; then
           # Special target build for i.MX 8DXL with V2X off
           bbnote "building ${SOC_TARGET} - ${REV_OPTION} V2X=NO ${target}"
           make SOC=${SOC_TARGET} DTB=${UBOOT_DTB_NAME} ${REV_OPTION} V2X=NO  flash_linux_m4
        else
           bbnote "building ${SOC_TARGET} - ${REV_OPTION} ${target}"
           make SOC=${SOC_TARGET} DTB=${UBOOT_DTB_NAME} ${REV_OPTION} ${target}
        fi
        if [ -e "${BOOT_STAGING}/flash.bin" ]; then
            cp ${BOOT_STAGING}/flash.bin ${S}/${BOOT_CONFIG_MACHINE}-${target}
        fi
    done
}
