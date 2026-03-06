#!/bin/bash

LLVM_VER="22.1.0"
# Edit this directory
LLVM_DIR=~/opt/llvm-$LLVM_VER

echo "[SCRIPT] Determining distro..."
DISTRO=$(cat /etc/*elease | grep DISTRIB_ID | cut -c 12-)
if [[ $DISTRO = "Ubuntu" ]]; then
    echo "[SCRIPT] Using Ubuntu!"
    echo "[SCRIPT] Changing APT mirror for faster download... [requires sudo]"
    sudo apt update
    sudo apt -y install ca-certificates
    sudo sed -i 's|http://archive.ubuntu.com|https://ftp.kaist.ac.kr|g' /etc/apt/sources.list.d/ubuntu.sources
    echo "[SCRIPT] Installing dependencies..."
    sudo apt update
    sudo apt -y install git g++ cmake ninja-build python3-setuptools zlib1g-dev libncurses-dev libstdc++6
else
    echo "[SCRIPT] Not using Ubuntu"
    echo "[SCRIPT] This script cannot configure dependencies for you. If build fails, make sure you have installed all necessary dependencies!"
fi

echo "[SCRIPT] Cloning LLVM-$LLVM_VER source..."
git clone -b llvmorg-$LLVM_VER https://github.com/llvm/llvm-project.git --depth 1
cd llvm-project

echo "[SCRIPT] Building and installing libc++..."
sleep 2
BSTRP_BUILD_DIR=build-$LLVM_VER-tmp
cmake -G Ninja -S llvm -B $BSTRP_BUILD_DIR \
    -DLLVM_ENABLE_PROJECTS="clang;lld" \
    -DLLVM_ENABLE_RUNTIMES="compiler-rt;libcxx;libcxxabi;libunwind" \
    -DLLVM_TARGETS_TO_BUILD=host \
    -DLLVM_RAM_PER_COMPILE_JOB=1024 \
    -DLLVM_RAM_PER_LINK_JOB=8192 \
    -DLLVM_RAM_PER_TABLEGEN_JOB=8192 \
    -DLLVM_USE_LINKER=gold \
    -DLLVM_BUILD_LLVM_DYLIB=ON \
    -DCMAKE_BUILD_TYPE=Release \
    -DCMAKE_INSTALL_PREFIX=$LLVM_DIR
cmake --build $BSTRP_BUILD_DIR && cmake --install $BSTRP_BUILD_DIR

if [[ $? -ne 0 ]]; then
    echo "[SCRIPT] Error while building libc++!"
    exit 1
fi

MACHINE_ARCH=$(uname -m)
LIBCXX_INCLUDE_PATH=$LLVM_DIR/include/c++/v1
LIBCXX_LIBRARY_PATH=$LLVM_DIR/lib/$MACHINE_ARCH-unknown-linux-gnu

echo "[SCRIPT] Building and installing LLVM..."
sleep 2
BUILD_DIR=build-$LLVM_VER
export PATH=$LLVM_DIR/bin:$PATH
cmake -G Ninja -S llvm -B $BUILD_DIR \
    -DCMAKE_C_COMPILER=clang \
    -DCMAKE_CXX_COMPILER=clang++ \
    -DCMAKE_INCLUDE_PATH="-I$LIBCXX_INCLUDE_PATH" \
    -DCMAKE_EXE_LINKER_FLAGS="-L$LIBCXX_LIBRARY_PATH -Wl,-rpath,$LIBCXX_LIBRARY_PATH" \
    -DCMAKE_SHARED_LINKER_FLAGS="-L$LIBCXX_LIBRARY_PATH -Wl,-rpath,$LIBCXX_LIBRARY_PATH" \
    -DLLVM_ENABLE_PROJECTS="clang;clang-tools-extra;lldb;lld" \
    -DLLVM_INSTALL_UTILS=ON \
    -DLLVM_TARGETS_TO_BUILD=host \
    -DLLVM_ENABLE_RTTI=ON \
    -DLLVM_ENABLE_EH=ON \
    -DLLVM_ENABLE_ASSERTIONS=ON \
    -DLLVM_RAM_PER_COMPILE_JOB=1024 \
    -DLLVM_RAM_PER_LINK_JOB=8192 \
    -DLLVM_RAM_PER_TABLEGEN_JOB=8192 \
    -DLLVM_ENABLE_LIBCXX=ON \
    -DLLVM_USE_LINKER=lld \
    -DLLVM_BUILD_LLVM_DYLIB=ON \
    -DCMAKE_BUILD_TYPE=Release \
    -DCMAKE_INSTALL_PREFIX=$LLVM_DIR
cmake --build $BUILD_DIR && cmake --install $BUILD_DIR

if [[ $? -ne 0 ]]; then
    echo "[SCRIPT] Error while building LLVM!"
    exit 1
fi
