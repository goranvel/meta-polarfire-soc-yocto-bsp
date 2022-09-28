SUMMARY = "Polarfire SoC AMP example applications"
DESCRIPTION = "Example FreeRTOS application to run in AMP build \
along with a Linux context"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE.md;md5=4396bf71d143500c4d9fa09c02527700"

DEPENDS = "makedepend-native"

inherit deploy

BRANCH = "main"
SRCREV="9a0816d28a7a746c37db3d61bbb1816bc2da9c8a"
SRC_URI = "git://bitbucket.microchip.com/scm/fpga_pfsoc_es/polarfire-soc-amp-examples.git;protocol=https;branch=${BRANCH}"

S = "${WORKDIR}/git"

EXT_CFLAGS += "--sysroot=${STAGING_DIR_TARGET}"
EXT_CFLAGS += "-DMPFS_HAL_FIRST_HART=4 -DMPFS_HAL_LAST_HART=4"

PARALLEL_MAKE = ""
EXTRA_OEMAKE = "REMOTE=1 REMOTEPROC=1 CROSS_COMPILE=${TARGET_PREFIX} EXT_CFLAGS='${EXT_CFLAGS}'"

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware
    install ${S}/mpfs-rpmsg-${AMP_DEMO}/Remote-Default/mpfs-rpmsg-remote.elf ${D}${nonarch_base_libdir}/firmware/rproc-miv-rproc-fw
}

do_compile() {
   oe_runmake -C ${S}/mpfs-rpmsg-${AMP_DEMO}
}

do_deploy() {
    install -m 755 ${S}/mpfs-rpmsg-${AMP_DEMO}/Remote-Default/mpfs-rpmsg-remote.elf ${DEPLOYDIR}/amp-application.elf
}

addtask deploy after do_install

COMPATIBLE_MACHINE = "(icicle-kit-es-amp)"

FILES:${PN} += "/lib/firmware/"