#!/usr/bin/env python3
"""
PC Controller Installer Builder for Multi-Modal Physiological Sensing Platform
Creates native installers for Windows, macOS, and Linux platforms
"""

import argparse
import json
import os
import platform
import shutil
import subprocess
import sys
import tempfile
import zipfile
from datetime import datetime
from pathlib import Path
from typing import Dict, List, Optional

class PCControllerInstallerBuilder:
    """
    Cross-platform installer builder for PC Controller with automated dependency management
    """
    
    def __init__(self, project_root: Path):
        self.project_root = project_root
        self.pc_controller_dir = project_root / "pc-controller"
        self.build_output_dir = project_root / "production_artifacts"
        
        # Platform-specific configuration
        self.platform_configs = {
            "windows": {
                "installer_type": "msi",
                "python_installer": "python-3.11.9-amd64.exe",
                "dependencies": ["PyQt6", "numpy", "pandas", "h5py", "pyqtgraph", "zeroconf"],
                "executable_name": "IRCamera-PCController.exe",
                "desktop_shortcut": True
            },
            "macos": {
                "installer_type": "pkg", 
                "python_installer": "python-3.11.9-macosx10.9.pkg",
                "dependencies": ["PyQt6", "numpy", "pandas", "h5py", "pyqtgraph", "zeroconf"],
                "executable_name": "IRCamera-PCController.app",
                "desktop_shortcut": False  # macOS uses Applications folder
            },
            "linux": {
                "installer_type": "deb",
                "python_installer": None,  # Use system Python
                "dependencies": ["python3-pyqt6", "python3-numpy", "python3-pandas", "python3-h5py"],
                "executable_name": "ircamera-pccontroller",
                "desktop_shortcut": True
            }
        }
        
    def validate_environment(self, platform_name: str) -> bool:
        """Validate build environment for target platform"""
        print(f"🔍 Validating build environment for {platform_name}...")
        
        # Check if PC Controller source exists
        if not self.pc_controller_dir.exists():
            print(f"❌ PC Controller source not found: {self.pc_controller_dir}")
            return False
            
        # Check platform-specific requirements
        platform_config = self.platform_configs.get(platform_name)
        if not platform_config:
            print(f"❌ Unsupported platform: {platform_name}")
            return False
            
        # Validate Python environment
        try:
            import PyQt6
            import numpy
            import pandas
            import h5py
            print("✅ Required Python dependencies available")
        except ImportError as e:
            print(f"❌ Missing Python dependency: {e}")
            return False
            
        print("✅ Build environment validated successfully")
        return True
        
    def create_portable_application(self, platform_name: str) -> Optional[Path]:
        """Create portable application bundle with all dependencies"""
        print(f"📦 Creating portable application for {platform_name}...")
        
        # Create temporary build directory
        build_dir = Path(tempfile.mkdtemp(prefix="ircamera_build_"))
        app_dir = build_dir / f"IRCamera-PCController-{platform_name}"
        app_dir.mkdir(parents=True)
        
        try:
            # Copy PC Controller source
            src_dir = app_dir / "src"
            shutil.copytree(self.pc_controller_dir / "src", src_dir)
            
            # Copy additional files
            files_to_copy = [
                "requirements.txt",
                "setup.py", 
                "README.md"
            ]
            
            for file_name in files_to_copy:
                src_file = self.pc_controller_dir / file_name
                if src_file.exists():
                    shutil.copy2(src_file, app_dir)
                    
            # Create virtual environment and install dependencies
            venv_dir = app_dir / "venv"
            self.create_virtual_environment(venv_dir, platform_name)
            
            # Create launcher scripts
            self.create_launcher_scripts(app_dir, platform_name)
            
            # Create configuration files
            self.create_configuration_files(app_dir, platform_name)
            
            print(f"✅ Portable application created: {app_dir}")
            return app_dir
            
        except Exception as e:
            print(f"❌ Failed to create portable application: {e}")
            if build_dir.exists():
                shutil.rmtree(build_dir)
            return None
            
    def create_virtual_environment(self, venv_dir: Path, platform_name: str):
        """Create isolated Python virtual environment with dependencies"""
        print(f"🐍 Creating virtual environment for {platform_name}...")
        
        # Create virtual environment
        subprocess.run([sys.executable, "-m", "venv", str(venv_dir)], check=True)
        
        # Get platform-specific paths
        if platform_name == "windows":
            pip_executable = venv_dir / "Scripts" / "pip.exe"
            python_executable = venv_dir / "Scripts" / "python.exe"
        else:
            pip_executable = venv_dir / "bin" / "pip"
            python_executable = venv_dir / "bin" / "python"
            
        # Upgrade pip
        subprocess.run([str(python_executable), "-m", "pip", "install", "--upgrade", "pip"], check=True)
        
        # Install dependencies
        platform_config = self.platform_configs[platform_name]
        for dependency in platform_config["dependencies"]:
            print(f"📥 Installing {dependency}...")
            subprocess.run([str(pip_executable), "install", dependency], check=True)
            
        print("✅ Virtual environment created successfully")
        
    def create_launcher_scripts(self, app_dir: Path, platform_name: str):
        """Create platform-specific launcher scripts"""
        print(f"📝 Creating launcher scripts for {platform_name}...")
        
        if platform_name == "windows":
            self.create_windows_launcher(app_dir)
        elif platform_name == "macos":
            self.create_macos_launcher(app_dir) 
        elif platform_name == "linux":
            self.create_linux_launcher(app_dir)
            
    def create_windows_launcher(self, app_dir: Path):
        """Create Windows batch launcher and executable"""
        # Create batch launcher
        launcher_bat = app_dir / "launch.bat"
        launcher_content = """@echo off
echo Starting IRCamera PC Controller...
set PYTHONPATH=%~dp0src
"%~dp0venv\\Scripts\\python.exe" "%~dp0src\\main.py" %*
if %ERRORLEVEL% neq 0 (
    echo.
    echo Error: Failed to start IRCamera PC Controller
    echo Please check that all dependencies are installed correctly
    pause
    exit /b %ERRORLEVEL%
)
"""
        
        with open(launcher_bat, 'w') as f:
            f.write(launcher_content)
            
        # Create PowerShell launcher for modern Windows
        launcher_ps1 = app_dir / "launch.ps1"
        ps_content = """# IRCamera PC Controller PowerShell Launcher
Write-Host "Starting IRCamera PC Controller..." -ForegroundColor Green

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$env:PYTHONPATH = Join-Path $scriptDir "src"
$pythonExe = Join-Path $scriptDir "venv\\Scripts\\python.exe"
$mainScript = Join-Path $scriptDir "src\\main.py"

try {
    & $pythonExe $mainScript $args
    if ($LASTEXITCODE -ne 0) {
        throw "Python script exited with code $LASTEXITCODE"
    }
} catch {
    Write-Host "Error: Failed to start IRCamera PC Controller" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    Write-Host "Please check that all dependencies are installed correctly" -ForegroundColor Yellow
    Read-Host "Press Enter to continue..."
    exit 1
}
"""
        
        with open(launcher_ps1, 'w') as f:
            f.write(ps_content)
            
    def create_macos_launcher(self, app_dir: Path):
        """Create macOS application bundle and launcher script"""
        # Create shell launcher
        launcher_sh = app_dir / "launch.sh"
        launcher_content = """#!/bin/bash
# IRCamera PC Controller macOS Launcher

set -e

echo "Starting IRCamera PC Controller..."

# Get script directory
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Set Python path
export PYTHONPATH="$DIR/src"

# Launch application
"$DIR/venv/bin/python" "$DIR/src/main.py" "$@"

if [ $? -ne 0 ]; then
    echo "Error: Failed to start IRCamera PC Controller"
    echo "Please check that all dependencies are installed correctly"
    read -p "Press Enter to continue..."
    exit 1
fi
"""
        
        with open(launcher_sh, 'w') as f:
            f.write(launcher_content)
            
        # Make launcher executable
        launcher_sh.chmod(0o755)
        
        # Create macOS app bundle structure
        app_bundle = app_dir / "IRCamera-PCController.app"
        contents_dir = app_bundle / "Contents"
        macos_dir = contents_dir / "MacOS"
        resources_dir = contents_dir / "Resources"
        
        for directory in [contents_dir, macos_dir, resources_dir]:
            directory.mkdir(parents=True, exist_ok=True)
            
        # Create Info.plist
        plist_content = """<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>CFBundleName</key>
    <string>IRCamera PC Controller</string>
    <key>CFBundleDisplayName</key>
    <string>IRCamera PC Controller</string>
    <key>CFBundleIdentifier</key>
    <string>com.ircamera.pccontroller</string>
    <key>CFBundleVersion</key>
    <string>1.0</string>
    <key>CFBundleShortVersionString</key>
    <string>1.0</string>
    <key>CFBundlePackageType</key>
    <string>APPL</string>
    <key>CFBundleExecutable</key>
    <string>IRCamera-PCController</string>
    <key>LSMinimumSystemVersion</key>
    <string>10.14</string>
    <key>NSHighResolutionCapable</key>
    <true/>
</dict>
</plist>
"""
        
        with open(contents_dir / "Info.plist", 'w') as f:
            f.write(plist_content)
            
        # Create executable launcher in app bundle
        app_executable = macos_dir / "IRCamera-PCController"
        app_launcher_content = """#!/bin/bash
# IRCamera PC Controller App Bundle Launcher

# Get bundle directory
BUNDLE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
APP_DIR="$(dirname "$BUNDLE_DIR")"

# Launch main application
exec "$APP_DIR/launch.sh" "$@"
"""
        
        with open(app_executable, 'w') as f:
            f.write(app_launcher_content)
            
        app_executable.chmod(0o755)
        
    def create_linux_launcher(self, app_dir: Path):
        """Create Linux launcher script and desktop entry"""
        # Create shell launcher
        launcher_sh = app_dir / "launch.sh"
        launcher_content = """#!/bin/bash
# IRCamera PC Controller Linux Launcher

set -e

echo "Starting IRCamera PC Controller..."

# Get script directory
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Set Python path
export PYTHONPATH="$DIR/src"

# Launch application
"$DIR/venv/bin/python" "$DIR/src/main.py" "$@"

if [ $? -ne 0 ]; then
    echo "Error: Failed to start IRCamera PC Controller"
    echo "Please check that all dependencies are installed correctly"
    read -p "Press Enter to continue..."
    exit 1
fi
"""
        
        with open(launcher_sh, 'w') as f:
            f.write(launcher_content)
            
        # Make launcher executable
        launcher_sh.chmod(0o755)
        
        # Create desktop entry
        desktop_entry = app_dir / "ircamera-pccontroller.desktop"
        desktop_content = """[Desktop Entry]
Version=1.0
Type=Application
Name=IRCamera PC Controller
Comment=Multi-Modal Physiological Sensing Platform Controller
Exec={install_dir}/launch.sh
Icon={install_dir}/resources/icon.png
Terminal=false
Categories=Science;Education;
StartupNotify=true
"""
        
        with open(desktop_entry, 'w') as f:
            f.write(desktop_content)
            
    def create_configuration_files(self, app_dir: Path, platform_name: str):
        """Create application configuration and resource files"""
        print("⚙️ Creating configuration files...")
        
        # Create resources directory
        resources_dir = app_dir / "resources"
        resources_dir.mkdir(exist_ok=True)
        
        # Create default configuration
        config_file = app_dir / "config.json"
        default_config = {
            "application": {
                "name": "IRCamera PC Controller",
                "version": "1.0.0",
                "platform": platform_name
            },
            "network": {
                "discovery_port": 5555,
                "data_port": 5556,
                "timeout_seconds": 30
            },
            "recording": {
                "default_session_duration": 300,
                "max_devices": 8,
                "sync_accuracy_ms": 5
            },
            "data": {
                "export_format": "hdf5",
                "compression": True,
                "backup_enabled": True
            },
            "ui": {
                "theme": "light",
                "auto_save": True,
                "show_advanced_options": False
            }
        }
        
        with open(config_file, 'w') as f:
            json.dump(default_config, f, indent=2)
            
        # Create requirements file for reference
        requirements_file = app_dir / "requirements_installed.txt"
        try:
            result = subprocess.run([sys.executable, "-m", "pip", "freeze"], 
                                  capture_output=True, text=True, check=True)
            with open(requirements_file, 'w') as f:
                f.write(result.stdout)
        except subprocess.CalledProcessError:
            print("⚠️ Could not generate installed requirements list")
            
    def create_installer(self, app_dir: Path, platform_name: str) -> Optional[Path]:
        """Create platform-specific installer package"""
        print(f"🔧 Creating installer for {platform_name}...")
        
        platform_config = self.platform_configs[platform_name]
        installer_type = platform_config["installer_type"]
        
        # Create installer based on platform
        if platform_name == "windows":
            return self.create_windows_installer(app_dir)
        elif platform_name == "macos":
            return self.create_macos_installer(app_dir)
        elif platform_name == "linux":
            return self.create_linux_installer(app_dir)
        else:
            print(f"❌ Unsupported platform for installer: {platform_name}")
            return None
            
    def create_windows_installer(self, app_dir: Path) -> Optional[Path]:
        """Create Windows MSI installer using WiX or NSIS"""
        print("🪟 Creating Windows MSI installer...")
        
        # For now, create a ZIP package with installation script
        # In production, this would use WiX Toolset or NSIS
        installer_dir = self.build_output_dir / "windows"
        installer_dir.mkdir(parents=True, exist_ok=True)
        
        zip_file = installer_dir / "IRCamera-PCController-Windows-Setup.zip"
        
        with zipfile.ZipFile(zip_file, 'w', zipfile.ZIP_DEFLATED) as zf:
            for root, dirs, files in os.walk(app_dir):
                for file in files:
                    file_path = Path(root) / file
                    arc_path = file_path.relative_to(app_dir)
                    zf.write(file_path, arc_path)
                    
        # Create installation script
        install_script = installer_dir / "install.bat"
        install_content = """@echo off
echo IRCamera PC Controller Installation
echo ===================================

set INSTALL_DIR=C:\\Program Files\\IRCamera-PCController

echo Creating installation directory...
if not exist "%INSTALL_DIR%" mkdir "%INSTALL_DIR%"

echo Extracting application files...
powershell -Command "Expand-Archive -Path 'IRCamera-PCController-Windows-Setup.zip' -DestinationPath '%INSTALL_DIR%' -Force"

echo Creating desktop shortcut...
powershell -Command "$WshShell = New-Object -comObject WScript.Shell; $Shortcut = $WshShell.CreateShortcut('%USERPROFILE%\\Desktop\\IRCamera PC Controller.lnk'); $Shortcut.TargetPath = '%INSTALL_DIR%\\launch.bat'; $Shortcut.WorkingDirectory = '%INSTALL_DIR%'; $Shortcut.Save()"

echo Installation completed successfully!
echo You can now launch IRCamera PC Controller from your desktop or from:
echo %INSTALL_DIR%\\launch.bat
pause
"""
        
        with open(install_script, 'w') as f:
            f.write(install_content)
            
        print(f"✅ Windows installer package created: {installer_dir}")
        return installer_dir
        
    def create_macos_installer(self, app_dir: Path) -> Optional[Path]:
        """Create macOS PKG installer"""
        print("🍎 Creating macOS PKG installer...")
        
        # For now, create a DMG-style directory
        # In production, this would use pkgbuild and productbuild
        installer_dir = self.build_output_dir / "macos"
        installer_dir.mkdir(parents=True, exist_ok=True)
        
        # Create application bundle in installer directory
        dmg_contents = installer_dir / "IRCamera-PCController-macOS"
        dmg_contents.mkdir(exist_ok=True)
        
        # Copy app bundle
        app_bundle = app_dir / "IRCamera-PCController.app"
        if app_bundle.exists():
            shutil.copytree(app_bundle, dmg_contents / "IRCamera-PCController.app")
            
        # Create installation instructions
        readme_file = dmg_contents / "README.txt"
        readme_content = """IRCamera PC Controller for macOS

Installation Instructions:
1. Drag IRCamera-PCController.app to your Applications folder
2. Launch the application from Applications or Launchpad
3. Follow the setup wizard to configure your sensors

System Requirements:
- macOS 10.14 or later
- Python 3.11 (installed automatically)
- 4GB RAM minimum, 8GB recommended
- 2GB free disk space

For support and documentation, visit the project repository.
"""
        
        with open(readme_file, 'w') as f:
            f.write(readme_content)
            
        print(f"✅ macOS installer package created: {installer_dir}")
        return installer_dir
        
    def create_linux_installer(self, app_dir: Path) -> Optional[Path]:
        """Create Linux DEB package"""
        print("🐧 Creating Linux DEB package...")
        
        installer_dir = self.build_output_dir / "linux"
        installer_dir.mkdir(parents=True, exist_ok=True)
        
        # Create DEB package structure
        deb_dir = installer_dir / "ircamera-pccontroller_1.0_amd64"
        debian_dir = deb_dir / "DEBIAN"
        usr_dir = deb_dir / "usr"
        
        debian_dir.mkdir(parents=True, exist_ok=True)
        (usr_dir / "local" / "bin").mkdir(parents=True, exist_ok=True)
        (usr_dir / "local" / "share" / "ircamera-pccontroller").mkdir(parents=True, exist_ok=True)
        (usr_dir / "share" / "applications").mkdir(parents=True, exist_ok=True)
        
        # Copy application files
        app_install_dir = usr_dir / "local" / "share" / "ircamera-pccontroller"
        shutil.copytree(app_dir, app_install_dir, dirs_exist_ok=True)
        
        # Create control file
        control_file = debian_dir / "control"
        control_content = """Package: ircamera-pccontroller
Version: 1.0
Section: science
Priority: optional
Architecture: amd64
Depends: python3 (>= 3.8), python3-pip, python3-venv
Maintainer: IRCamera Development Team
Description: Multi-Modal Physiological Sensing Platform PC Controller
 IRCamera PC Controller provides a comprehensive interface for managing
 multi-modal physiological sensing sessions with Android devices.
 Includes real-time monitoring, data aggregation, and export capabilities.
"""
        
        with open(control_file, 'w') as f:
            f.write(control_content)
            
        # Create executable symlink
        bin_link = usr_dir / "local" / "bin" / "ircamera-pccontroller"
        with open(bin_link, 'w') as f:
            f.write(f"""#!/bin/bash
exec /usr/local/share/ircamera-pccontroller/launch.sh "$@"
""")
        bin_link.chmod(0o755)
        
        # Copy desktop entry
        desktop_src = app_dir / "ircamera-pccontroller.desktop"
        desktop_dst = usr_dir / "share" / "applications" / "ircamera-pccontroller.desktop"
        if desktop_src.exists():
            # Update desktop entry with installed paths
            with open(desktop_src, 'r') as f:
                desktop_content = f.read()
            
            desktop_content = desktop_content.format(
                install_dir="/usr/local/share/ircamera-pccontroller"
            )
            
            with open(desktop_dst, 'w') as f:
                f.write(desktop_content)
        
        # Create installation scripts
        postinst_file = debian_dir / "postinst"
        postinst_content = """#!/bin/bash
set -e

echo "Setting up IRCamera PC Controller..."

# Update desktop database
if command -v update-desktop-database >/dev/null 2>&1; then
    update-desktop-database /usr/share/applications
fi

echo "IRCamera PC Controller installed successfully!"
echo "Launch with: ircamera-pccontroller"
echo "Or find it in your applications menu."
"""
        
        with open(postinst_file, 'w') as f:
            f.write(postinst_content)
        postinst_file.chmod(0o755)
        
        # Build DEB package (requires dpkg-deb)
        try:
            deb_file = installer_dir / "ircamera-pccontroller_1.0_amd64.deb"
            subprocess.run(["dpkg-deb", "--build", str(deb_dir), str(deb_file)], check=True)
            print(f"✅ Linux DEB package created: {deb_file}")
            return installer_dir
        except (subprocess.CalledProcessError, FileNotFoundError):
            print("⚠️ dpkg-deb not available, creating tar.gz archive instead")
            
            # Fallback: create tar.gz archive
            tar_file = installer_dir / "ircamera-pccontroller_1.0_linux.tar.gz"
            subprocess.run(["tar", "-czf", str(tar_file), "-C", str(installer_dir), 
                          deb_dir.name], check=True)
            
            # Create installation script
            install_script = installer_dir / "install.sh"
            install_content = """#!/bin/bash
# IRCamera PC Controller Linux Installation Script

set -e

echo "IRCamera PC Controller Linux Installation"
echo "========================================"

if [ "$EUID" -ne 0 ]; then
    echo "Please run with sudo for system-wide installation"
    exit 1
fi

echo "Extracting application..."
tar -xzf ircamera-pccontroller_1.0_linux.tar.gz

echo "Installing files..."
cp -r ircamera-pccontroller_1.0_amd64/* /

echo "Setting permissions..."
chmod +x /usr/local/bin/ircamera-pccontroller
chmod +x /usr/local/share/ircamera-pccontroller/launch.sh

echo "Updating desktop database..."
if command -v update-desktop-database >/dev/null 2>&1; then
    update-desktop-database /usr/share/applications
fi

echo "Installation completed successfully!"
echo "Launch with: ircamera-pccontroller"
"""
            
            with open(install_script, 'w') as f:
                f.write(install_content)
            install_script.chmod(0o755)
            
            print(f"✅ Linux installation package created: {installer_dir}")
            return installer_dir

def main():
    """Main entry point for PC Controller installer builder"""
    parser = argparse.ArgumentParser(
        description="PC Controller Installer Builder for Multi-Modal Physiological Sensing Platform"
    )
    parser.add_argument("--platform",
                       choices=["windows", "macos", "linux", "all"],
                       default="all",
                       help="Target platform for installer")
    parser.add_argument("--output-dir",
                       type=Path,
                       help="Output directory for installer packages")
    parser.add_argument("--portable-only",
                       action="store_true",
                       help="Create portable application only (skip installer)")
    
    args = parser.parse_args()
    
    # Initialize builder
    project_root = Path("/home/runner/work/IRCamera/IRCamera")
    builder = PCControllerInstallerBuilder(project_root)
    
    # Set custom output directory if provided
    if args.output_dir:
        builder.build_output_dir = args.output_dir
        
    print("🏭 IRCamera PC Controller Installer Builder")
    print("=" * 50)
    
    # Determine target platforms
    if args.platform == "all":
        target_platforms = ["windows", "macos", "linux"]
    else:
        target_platforms = [args.platform]
        
    success_count = 0
    
    for platform_name in target_platforms:
        print(f"\n🔧 Building installer for {platform_name.upper()}...")
        
        # Validate environment
        if not builder.validate_environment(platform_name):
            print(f"❌ Skipping {platform_name} due to validation errors")
            continue
            
        # Create portable application
        app_dir = builder.create_portable_application(platform_name)
        if not app_dir:
            print(f"❌ Failed to create portable application for {platform_name}")
            continue
            
        # Create installer package unless portable-only requested
        if not args.portable_only:
            installer_dir = builder.create_installer(app_dir, platform_name)
            if installer_dir:
                print(f"✅ {platform_name.upper()} installer created successfully")
                success_count += 1
            else:
                print(f"❌ Failed to create installer for {platform_name}")
        else:
            print(f"✅ {platform_name.upper()} portable application created")
            success_count += 1
            
        # Clean up temporary app directory
        if app_dir.exists() and app_dir.name.startswith("ircamera_build_"):
            shutil.rmtree(app_dir.parent)
            
    print(f"\n🎉 Installer Build Complete!")
    print(f"✅ Successfully built {success_count}/{len(target_platforms)} installers")
    print(f"📦 Output directory: {builder.build_output_dir}")
    
    if success_count > 0:
        print("\n📚 Next steps:")
        print("1. Test installers on target platforms")
        print("2. Distribute via appropriate channels") 
        print("3. Update documentation with installation instructions")
    else:
        print("\n❌ No installers were created successfully")
        sys.exit(1)

if __name__ == "__main__":
    main()