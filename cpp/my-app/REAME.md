# my-app

## Setup and Execution
1. Install [clang](https://clang.llvm.org/), [cmake](https://cmake.org/), [git](https://git-scm.com/), and [ninja](https://cmake.org/cmake/help/latest/generator/Ninja.html)
1. Optionally, consider also installing [lldb](https://lldb.llvm.org/) for debugging
1. Clone this repo
1. Navigate to this repo from terminal and run
    - `mkdir build`
    - `cd build`
    - `cmake -G Ninja ..`
    - `ninja`
1. Run the application with `./build/app_executable`
1. To create a distributable
    - `cd build`
    - `cmake --install . --prefix ./dist`


## Development Environment
 - [Code - OSS](https://github.com/microsoft/vscode) with the following Extensions:
    - [clangd](https://open-vsx.org/vscode/item?itemName=llvm-vs-code-extensions.vscode-clangd)
    - [CMake Tools](https://open-vsx.org/vscode/item?itemName=ms-vscode.cmake-tools)
    - [CodeLLDB](https://open-vsx.org/vscode/item?itemName=vadimcn.vscode-lldb)
   - [Code Spell Checker by Street Side Software](https://open-vsx.org/vscode/item?itemName=streetsidesoftware.code-spell-checker)
 - [git](https://git-scm.com/about)
 - [GitHub](https://github.com)
 - [CachyOS](https://cachyos.org/)

 For debugging and execution I'm using the CMake Tools plugin.

