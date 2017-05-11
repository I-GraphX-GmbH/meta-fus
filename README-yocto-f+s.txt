fsimx6sx-Y0.6
=============

This is an F&S pre-release for Yocto.

This release is based on the NXP/Freescale Release BSP of Yocto, which
in turn is based on the NXP/Freescale Community BSP, which in turn is
based on Yocto 2.1.1 (Krogoth). It provides a U-Boot based on
u-boot-2014.07 and a Linux kernel based on 4.1.15.

The following F&S standard images were tested:

Image           Architecture   X11 size    Wayland Size
---------------------------------------------------------
fus-image-std   fsimx6         109 MB      86 MB
                fsimx6sx       109 MB      85 MB
                fsimx6ul        94 MB      -
fus-image-qt5   fsimx6         228 MB      201 MB
                fsimx6sx       227 MB      200 MB
                fsimx6ul       212 MB      -

Remark:

For historical reasons, the company name "Freescale" and the
abbreviation "fsl" appear at several places in Yocto, despite the fact
that this company has merged with NXP in the meantime and is called
NXP now. We will use NXP/Freescale where appropriate to make this
relation as clear as possible.


About Yocto
-----------

Yocto is a system to build a Linux distribution. So it is *not* a
Linux distribution by itself, but it will help *building* a Linux
distribution. This includes the toolchains, the bootloader, the
kernel, the root filesystems and Yocto can even build a package
repository where the end user can install additional packages from.

Yocto is organized in layers. Layers may exist next to each other, but
usually they have a hierarchical order, where the next layer is based
on the previous layer. The F&S yocto release is actually only the
NXP/Freescale Release BSP, where an additional F&S layer is added on top
to provide support for the boards and modules from F&S. The NXP/Freescale
Release BSP itself is based on the regular Yocto release and
simlpy adds some NXP/Freescale layers, to support the NXP CPUs and
Evaluation Boards. Yocto itself is again based on Open Embedded, and
adds some additional Yocto specific software layers, called "poky".

So at the core, Yocto is an Open Embedded based system. Open Embedded
uses recipes, like they are used when baking a cake. The recipe
describes what you need as ingredients, and then it tells step by step
what tasks have to be done to produce the cake. So a recipe in Yocto
also names the ingredients (source packages, patch files,
configuration settings, etc.) and then tells step by step what tasks
have to be done to produce the target. For example how sources are
unpacked and patched, how they are "converted" to binaries, how tools
are installed to help the build process, how different files are
grouped to root filesystems or other images, and so on. To add new
packages or build strategies, you simply have to add some more
recipes. This makes Yocto/Open Embedded a rather powerful build system
where you can have any influence that you like.

On the other hand you actually have to write a seperate recipe even
for small tasks. Even if you just want to assemble your own set of
packages to build your private root filesystem, you need to write a
recipe. There is no easy menu or GUI based package selection like for
example in Buildroot. Everything works with recipes. Of course you can
build some default images, but at the moment where you want to do
something different, you have to modify an existing recipe or you have
to write your own one.

So you actually have to learn how to write recipes if you want to work
with Yocto. There is no way around this. It may also be necessary to
learn how configurations and layers are written, because the most
simple way to add your own application is by adding a separate layer
for it, on top of the provided Yocto eco system.

These recipes use an own skripting language, based on Python. And
there is a program called bitbake that can parse these recipes and
execute the steps described in them. So building a Yocto package, root
filesystem or even full target image as a whole, you have to use
bitbake.

At the first invocation bitbake parses all existing recipes and builds
a cache for them. This cache speeds up later calls considerably,
because parsing the recipes can already take a few minutes and this
reduces to a few seconds when the cache can be re-used. Then bitbake
determines what tasks have to be done to reach the desired target. A
task may be fetching a package via the internet, unpack a package,
configure, compile, install a package or similar steps. It then
generates a list of these tasks and then executes them.

If you have a multi-core system, bitbake can work in parallel. It
knows the dependencies between the tasks and if possible, it already
executes the next task(s) in parallel, up to a certain limit that can
be given in the configuration. But please keep in mind that each task
itself may be a complicated build process that may use parallel
execution, for example a "make" with many parallel threads. So don't
overdo the settings, because the number of parallel bitbake tasks will
multiply with the number of parallel executions of the tasks
themselves.

If execution fails at some task, bitbake finishes all the other tasks
that are running in parallel, but it will not start any new tasks.
When all pending tasks are completed, it will stop with an error
message. bitbake logs all steps that it does in log files, one file
per task. So you can reconstruct at which step the build process
failed. Then you can fix the problem and when you call bitbake again,
it will skip the tasks that it had already finished successfully and
restart at the point where it had left off.

You can also interrupt the build process by hand, by typing Ctrl-C.
However bitbake will *not* stop immediately. Instead like in the error
case, it first finishes all running tasks and then stops. You can
continue by issuing the bitbake command again.


Installation
------------

First of all, unpack the F&S Yocto release. Yocto-only releases are
marked with a 'Y' instead of a 'V' for the release version number.

  tar xvf fsimx6sx-Y0.6.tar.bz2
  cd fsimx6sx-Y0.6

As mentioned above, the F&S Yocto release uses different layers from
different sources. When installing, these layers are actually
downloaded from different GIT repositories. Only the F&S layer is
already here inside the F&S Yocto release tar archive. This layer
simply provides the U-Boot and Linux kernel for all supported F&S
boards.

To download the remaining layers, simply call this skript:

  ./yocto-download

This will take a few minutes, depending on your internet speed.

The download is done by using a small tool called "repo" that is
designed to handle several GIT repositories in parallel. So the first
step of the skript is to download the repo tool. Then it fetches the
repo description for the NXP/Freescale Release BSP and finally lets
repo download all the different GIT repositories with the Yocto
layers.

- fsl-community-bsp-base
- meta-fsl-arm
- meta-fsl-arm-extra
- meta-fsl-demos
- meta-fsl-bsp-release
- meta-browser
- meta-qt5
- poky
- meta-openembedded

At the end it simply adds the F&S layer (called meta-fus) to the
configuration file bblayers.conf.


Configure for a board
---------------------

To configure for a specific F&S board, simply call

  DISTRO=<distro> MACHINE=<arch> . fsl-setup-release.sh -b <build-dir>

Please note the extra dot that is used to run the fsl-setup-release.sh
(and the corresponding setup-environment script) in the current shell
by sourcing it and not spawning a new sub-shell.

<distro> is one of the possible display solutions, one of:

  fus-imx-x11        Only X11 graphics
  fus-imx-wayland    Wayland/weston graphics
  fus-imx-xwayland   Wayland & X11 graphics; No EGL on X11 applications
  fus-imx-fb         Just framebuffer graphics, no X11, no Wayland

<arch> can be one of the supported F&S architecture families:

  fsimx6             Regular i.MX6 boards with Solo/DualLite/Quad CPU
                     (armStoneA9, armStoneA9r2, efusA9, PicoMODA9, NetDCUA9)
  fsimx6sx           Boards with i.MX6-SoloX CPU (efusA9X, PicoCOMA9X)
  fsimx6ul           Boards with i.MX6-UltraLite CPU (efusA7UL, PicoCOM1.2)

<build-dir> is the name of the build directory to create. We recommend
a name that starts with "build" and holds the name of the architecture and
the distro, e.g. "build-fsimx6sx-x11". This command will automatically
switch to the newly created directory and sets the environment so that
everything runs smoothly.

You have to repeat this step in every new shell that should be used to
build this Yocto image. For example if you restart your PC, you have
to call this command again to set up the environment correctly again.


Build an image
--------------

To build one of the demo images, simply call

  bitbake <image-name>

Here <image-name> is one of the images listed above, for example
fus-image-std. This will download all the required source packages,
patch them, configure them, compile them, and assemble them in a
package system and in target images. This includes binaries for the
bootloader, the Linux kernel and the root filesystem. Even the
toolchain, that is used for compiling these packages is built during
this process. Therefore building can take quite a long time, even
several hours. For example the rather complex fsl-image-machine-test
took about 6 hours on a PC with a rather fast quad core CPU with
hyper-threading. It also requires quite a lot of free disk space. We
recommend having at least 50GB of free disk space, the more the
better.

The resulting files can be found in

  tmp/deploy/images

The names are as follows. Please replace <arch> with the architecture
name that you configured your Yocto system with, e.g. fsimx6sx, and
<image-name> with the name of the target image that you have built.

  uboot.nb0                    U-Boot binary (suited for NBoot)
  uImage                       Linux kernel image for <arch>
  uImage-<board>.dtb           Device trees for all boards of <arch>
  <image-name>-<arch>.ubifs    Image to be installed in NAND flash
  <image-name>-<arch>.ext3     ext3 image, e.g. to be used via NFS
  <image-name>-<arch>.sdcard   Image to be installed on an SD card

If the build process fails at some time, you can fix the error and
continue by calling the bitbake command again. This will skip all the
steps that were already done and only completes the missing stages.

Usually you can also build additional images without having to erase
the previous results. Just call bitbake with the new image name. Again
only those steps that are new for the new image are done by bitbake.
However you have to keep in mind that in some cases the compilation
has to be done differently for different images and that some already
completed task may not fit. For example some graphical programs have
to be configured differently, if Wayland or DirectFB is used instead
of the standard X.org X-Server. Then you would have to re-do such
tasks by hand and it is rather difficult to tell which ones. So if an
image fails to build or run, it may be better to restart the build
process completely anew.

Remark:

Some images are rather large and may not fit into the NAND flash
memory of your board or module. So for example fsl-image-multimedia
results in a file of about 160MB which does not fit into the 128MB of
the regular armStoneA9, so it can not be run from NAND flash. Such a
rootfs can only be mounted via NFS or SD card then.

In addition, the fsl-image-machine-test creates an UBIFS image with a
size of about 300MB. The UBIFS images are configured to be at most
256MB of size. To compile this image, you have to modify the file

  sources/meta-fus/conf/distro/include/fus-common.inc

to avoid an overflow of the UBIFS rootfs. Replace the line

  MKUBIFS_ARGS = "-m 2048 -e 126976 -c 2048"

with this content:

  MKUBIFS_ARGS = "-m 2048 -e 126976 -c 2800"

We have included the U-Boot binary, the kernel image, the kernel
device tree binary and a few root filesystems images in the binaries
subdirectory. You can download them directly to the board to try them
out. The default display is LCD with a resolution of 800x480 pixels.


Rebuild a single package
------------------------

bitbake needs a recipe as target name. Target images are also only
recipes. But you can also execute smaller entities, for example a
smaller sub-target or even a single package. Everything that has a
separate recipe can be processed separately with bitbake.

Usually when a package is built, different stages are executed. For
example

  fetch, unpack, patch, configure, compile, strip, install,
  package, deploy

The list may vary from package to package. For example when building a
Linux kernel, there are further steps to build the kernel modules and
the device tree binaries. In addition there is also always the special
stage named "clean" to remove a package from the system, for example
to rebuild it. You can show the list of possible task of a package
with the command

  bitbake -c listtasks <package>

For example:

  bitbake -c listtasks linux-fus

Therefore to rebuild a single package, you usually execute the
following sequence of commands. They clean and recompile the package
and then add it again to the final target image.

  bitbake -c clean <package>
  bitbake -c compile <package>
  bitbake <image-name>

Here are some other examples, using the linux-fus package. To call
menuconfig for the kernel, call:

  bitbake -c menuconfig linux-fus

To copy the kernel to the deploy/images directory:

  bitbake -c deploy linux-fus

Open a shell in the package directory where you can issue arbitrary
commands:

  bitbake -c devshell linux-fus


Add packages to an image
------------------------

If you want to add a package to your local build (in addition to the
regular image content), you have to go to your build directory and
open the file ./conf/local.conf. Here yo can add the package by
listing it in IMAGE_INSTALL_append. Sometimes you also have to do
additional steps like enabling a specific software license in variable
LICENSE_FLAGS_WHITELIST. If the software is in a separate layer, check
if the layer is already in conf/bblayers.conf and add the layer if it
is missing.

For example to add the Chromium browser on top of core-image-x11, the
following two lines need to be present in the BBLAYERS variable in
bblayers.conf. (This should be the case if you set up the build
directory with fsl-setup-release.sh like shown above.)

  ${BSPDIR}/sources/meta-openembedded/meta-gnome \
  ${BSPDIR}/sources/meta-browser \

Then you have to add the following lines to local.conf:

  LICENSE_FLAGS_WHITELIST = " commercial_libav commercial_x264 commercial"
  IMAGE_INSTALL_append = " chromium libexif"

After these preparations, you can build your image:

  bitbake core-image-x11

Please note that building this image with Chromium needs lots of
memory and time. About 50 GB of free hard disc space, about 11 GB of
RAM when linking and more than eight hours build time even on a very
fast computer. So do not try this on a small build machine, it will
fail.

Adding firefox works rather similar, here you need these lines instead:

  LICENSE_FLAGS_WHITELIST = " commercial_libav commercial_x264 commercial"
  IMAGE_INSTALL_append = " firefox libexif"



Install U-Boot on the board
---------------------------

To install the U-Boot image on the board, start the board and go into
NBoot. This is done by pressing 's' (lower-case S) until it repeats,
then restart the board. Use a terminal program that supports 1:1
binary download, for example DCUTermi from F&S or Tera Term.

If you are coming from a different environment, for example from a
Buildroot based environment, we recommend erasing the whole NAND flash
with command 'E' (upper-case e) here.

Now press 'd' (lower-case D) to start the serial download. Then send
the file "uboot.nb0" to the board. After download is complete, press
'f' (lower-case F) to save U-Boot to NAND flash. Now restart the board
or simply press 'x' (lower-case X) to start U-Boot directly from the
loaded image in RAM.


Install kernel image and rootfs
-------------------------------

On the PC, the images have to be copied to a directory from where they
can be downloaded via TFTP. So you need to have a TFTP server running
on your PC. When you are following the instructions of the F&S
documentation "AdvicedForLinuxOnPC_eng.pdf" for how to do this, you
have a directory /tftpboot for this purpose. So copy the following
files to it:

  cp uImage /tftpboot
  cp uImage-<board>.dtb /tftpboot
  cp <image-name>-<arch>.ubifs /tftpboot

Now on your board go to U-Boot. This is done by pressing a key while
the Autostart countdown counts down. If you have not done it already,
set the correct network configuration variables, i.e. ethaddr,
gateway, netmask, and of course serverip to the IP address of your PC.
Save these settings with saveenv.

Download and save the kernel image:

  tftp uImage
  nand erase.part Kernel
  nand write $loadaddr Kernel $filesize

Download and save the device tree data:

  tftp uImage-<board>.dtb
  nand erase.part FTD
  nand write $loadaddr FDT $filesize

Download and save the rootfs image:

  ubi part TargetFS
  tftp <image-name>-<arch>.ubifs
  ubi write $loadaddr rootfs $filesize

Finally make sure that the boot strategy is set up for device trees.

  run _kernel_nand
  run _rootfs_ubifs
  run _fdt_nand
  saveenv

Now start the linux system with "boot" or simply restart the board.


Useful utilities
----------------

If you run

  source ../yocto-f+s-utilities

in the build directory, you will get the following small helper
functions:

list_f+s_target_boards      Show a list of all supported F&S boards
list_target_images          Show all recipes with "image" in their name
list_all_recipes            List all available recipes (list is long!)
list_target_image_packages  List all the packages that are part of the
                            target image given as argument

For example to determine which packages are part of core-image-sato,
call

  list_target_image_packages core-image-sato


Further reading
---------------

There is quite a lot of documentation available at the Yocto homepage

  https://www.yoctoproject.org/documentation
  https://www.yoctoproject.org/documentation/active

In addition you can also have a look at the documentation of the
NXP/Freescale Community BSP on github:

  https://github.com/Freescale/Documentation

There is also a fine tool called bb that allows inspecting Yocto
variables, list recipes and providers, show bitbake logs, search
packages and recipes, show package contents and dependency structures,
edit recipes, include and bbappend files, and similar things. You can
clone it from github:

  git clone https://github.com/kergoth/bb

Then read README.md for installation instructions.
