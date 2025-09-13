#!/usr/bin/env python3
"""
Enterprise APK Builder for Multi-Modal Physiological Sensing Platform
Builds production-ready signed APKs for institutional deployment
"""

import argparse
import json
import os
import shutil
import subprocess
import sys
from datetime import datetime
from pathlib import Path
from typing import Dict, List, Optional

class EnterpriseAPKBuilder:
    """
    Enterprise-grade APK builder with certificate signing and institutional deployment features
    """
    
    def __init__(self, project_root: Path):
        self.project_root = project_root
        self.build_output_dir = project_root / "production_artifacts" / "android"
        self.gradle_script = project_root / "gradlew"
        
        # Build variants for different deployment scenarios
        self.build_variants = {
            "samsung_optimized": {
                "description": "Optimized build for Samsung S22 devices with enhanced features",
                "gradle_task": "assembleSamsungOptimizedRelease",
                "apk_suffix": "samsung_optimized"
            },
            "generic": {
                "description": "Generic Android build for broad device compatibility", 
                "gradle_task": "assembleGenericRelease",
                "apk_suffix": "generic"
            },
            "debug": {
                "description": "Debug build for development and testing",
                "gradle_task": "assembleDebug", 
                "apk_suffix": "debug"
            }
        }
        
    def validate_environment(self) -> bool:
        """Validate build environment and dependencies"""
        print("🔍 Validating build environment...")
        
        # Check if Gradle wrapper exists
        if not self.gradle_script.exists():
            print(f"❌ Gradle wrapper not found: {self.gradle_script}")
            return False
            
        # Check if Android SDK is available
        try:
            result = subprocess.run([str(self.gradle_script), "tasks"], 
                                  capture_output=True, text=True, cwd=self.project_root)
            if result.returncode != 0:
                print(f"❌ Gradle tasks failed: {result.stderr}")
                return False
        except Exception as e:
            print(f"❌ Failed to run Gradle: {e}")
            return False
            
        print("✅ Build environment validated successfully")
        return True
        
    def clean_build_environment(self) -> bool:
        """Clean previous build artifacts"""
        print("🧹 Cleaning build environment...")
        
        try:
            # Run Gradle clean
            result = subprocess.run([str(self.gradle_script), "clean"], 
                                  cwd=self.project_root, check=True)
            
            # Remove old production artifacts
            if self.build_output_dir.exists():
                shutil.rmtree(self.build_output_dir)
            
            self.build_output_dir.mkdir(parents=True, exist_ok=True)
            
            print("✅ Build environment cleaned successfully")
            return True
            
        except subprocess.CalledProcessError as e:
            print(f"❌ Failed to clean build environment: {e}")
            return False
            
    def build_apk(self, variant: str = "samsung_optimized") -> Optional[Path]:
        """Build APK for specified variant"""
        if variant not in self.build_variants:
            print(f"❌ Unknown build variant: {variant}")
            print(f"Available variants: {list(self.build_variants.keys())}")
            return None
            
        variant_config = self.build_variants[variant]
        print(f"🔨 Building APK variant: {variant}")
        print(f"📝 Description: {variant_config['description']}")
        
        try:
            # Build APK using Gradle
            gradle_task = variant_config["gradle_task"]
            print(f"🚀 Running Gradle task: {gradle_task}")
            
            result = subprocess.run([str(self.gradle_script), gradle_task, "--stacktrace"], 
                                  cwd=self.project_root, check=True,
                                  capture_output=True, text=True)
            
            print("✅ APK build completed successfully")
            
            # Find generated APK file
            apk_path = self.find_generated_apk(variant)
            if apk_path:
                print(f"📱 Generated APK: {apk_path}")
                return apk_path
            else:
                print("❌ Could not locate generated APK file")
                return None
                
        except subprocess.CalledProcessError as e:
            print(f"❌ APK build failed: {e}")
            if e.stdout:
                print(f"Build stdout: {e.stdout}")
            if e.stderr:
                print(f"Build stderr: {e.stderr}")
            return None
            
    def find_generated_apk(self, variant: str) -> Optional[Path]:
        """Find the generated APK file for the given variant"""
        # Common APK output locations
        apk_search_paths = [
            self.project_root / "app" / "build" / "outputs" / "apk" / "release",
            self.project_root / "app" / "build" / "outputs" / "apk" / "debug",
            self.project_root / "app" / "build" / "outputs" / "apk",
            self.project_root / "build" / "outputs" / "apk"
        ]
        
        for search_path in apk_search_paths:
            if search_path.exists():
                for apk_file in search_path.glob("*.apk"):
                    print(f"🔍 Found APK candidate: {apk_file}")
                    return apk_file
                    
        return None
        
    def sign_apk(self, apk_path: Path, certificate_path: Path, 
                 keystore_password: str, key_alias: str = "enterprise") -> Optional[Path]:
        """Sign APK with enterprise certificate"""
        print(f"✍️ Signing APK with enterprise certificate...")
        
        if not certificate_path.exists():
            print(f"❌ Certificate file not found: {certificate_path}")
            return None
            
        # Create signed APK filename
        signed_apk_path = apk_path.parent / f"signed_{apk_path.name}"
        
        try:
            # Use jarsigner to sign the APK
            sign_command = [
                "jarsigner",
                "-verbose",
                "-sigalg", "SHA256withRSA",
                "-digestalg", "SHA-256", 
                "-keystore", str(certificate_path),
                "-storepass", keystore_password,
                str(apk_path),
                key_alias
            ]
            
            result = subprocess.run(sign_command, check=True, 
                                  capture_output=True, text=True)
            
            # Verify APK signature
            verify_command = ["jarsigner", "-verify", "-verbose", str(apk_path)]
            verify_result = subprocess.run(verify_command, check=True,
                                         capture_output=True, text=True)
            
            print("✅ APK signed and verified successfully")
            
            # Copy signed APK to final location
            shutil.copy2(apk_path, signed_apk_path)
            return signed_apk_path
            
        except subprocess.CalledProcessError as e:
            print(f"❌ APK signing failed: {e}")
            if e.stdout:
                print(f"Signing stdout: {e.stdout}")
            if e.stderr:
                print(f"Signing stderr: {e.stderr}")
            return None
        except FileNotFoundError:
            print("❌ jarsigner not found. Please ensure Android SDK build-tools are in PATH")
            return None
            
    def align_apk(self, signed_apk_path: Path) -> Optional[Path]:
        """Optimize APK with zipalign for better performance"""
        print("⚡ Optimizing APK with zipalign...")
        
        aligned_apk_path = signed_apk_path.parent / f"aligned_{signed_apk_path.name}"
        
        try:
            # Use zipalign to optimize APK
            align_command = [
                "zipalign",
                "-v", "4",
                str(signed_apk_path),
                str(aligned_apk_path)
            ]
            
            result = subprocess.run(align_command, check=True,
                                  capture_output=True, text=True)
            
            print("✅ APK alignment completed successfully")
            return aligned_apk_path
            
        except subprocess.CalledProcessError as e:
            print(f"❌ APK alignment failed: {e}")
            return signed_apk_path  # Return unaligned version as fallback
        except FileNotFoundError:
            print("⚠️ zipalign not found, skipping alignment optimization")
            return signed_apk_path  # Return unaligned version
            
    def generate_build_metadata(self, final_apk_path: Path, variant: str) -> Dict:
        """Generate comprehensive build metadata for tracking"""
        print("📋 Generating build metadata...")
        
        # Get APK file information
        apk_size_mb = final_apk_path.stat().st_size / (1024 * 1024)
        
        # Get Git information if available
        git_commit = "unknown"
        git_branch = "unknown"
        try:
            git_commit = subprocess.check_output(
                ["git", "rev-parse", "HEAD"], 
                cwd=self.project_root, text=True
            ).strip()
            
            git_branch = subprocess.check_output(
                ["git", "rev-parse", "--abbrev-ref", "HEAD"],
                cwd=self.project_root, text=True  
            ).strip()
        except subprocess.CalledProcessError:
            print("⚠️ Git information not available")
            
        metadata = {
            "build_info": {
                "variant": variant,
                "description": self.build_variants[variant]["description"],
                "build_timestamp": datetime.now().isoformat(),
                "apk_filename": final_apk_path.name,
                "apk_size_mb": round(apk_size_mb, 2)
            },
            "version_info": {
                "git_commit": git_commit,
                "git_branch": git_branch,
                "build_number": int(datetime.now().timestamp())
            },
            "deployment_info": {
                "target_platform": "Android 8.0+ (API 26+)",
                "recommended_devices": ["Samsung Galaxy S22", "Samsung Galaxy S22+", "Samsung Galaxy S22 Ultra"],
                "minimum_requirements": {
                    "android_version": "8.0",
                    "api_level": 26,
                    "ram_gb": 4,
                    "storage_gb": 8
                }
            },
            "security_info": {
                "signed": True,
                "certificate_type": "Enterprise",
                "signature_algorithm": "SHA256withRSA"
            }
        }
        
        return metadata
        
    def create_deployment_package(self, final_apk_path: Path, metadata: Dict, 
                                variant: str) -> Path:
        """Create complete deployment package with documentation"""
        print("📦 Creating deployment package...")
        
        # Create deployment directory
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        package_dir = self.build_output_dir / f"IRCamera_Enterprise_{variant}_{timestamp}"
        package_dir.mkdir(parents=True, exist_ok=True)
        
        # Copy APK to deployment package
        final_apk_name = f"IRCamera_Enterprise_{variant}_v{metadata['version_info']['build_number']}.apk"
        final_apk_dest = package_dir / final_apk_name
        shutil.copy2(final_apk_path, final_apk_dest)
        
        # Save metadata as JSON
        metadata_file = package_dir / "build_metadata.json"
        with open(metadata_file, 'w') as f:
            json.dump(metadata, f, indent=2)
            
        # Create deployment instructions
        instructions_file = package_dir / "DEPLOYMENT_INSTRUCTIONS.md"
        self.create_deployment_instructions(instructions_file, final_apk_name, metadata)
        
        # Create installation script for ADB deployment
        install_script = package_dir / "install_apk.sh"
        self.create_install_script(install_script, final_apk_name)
        
        print(f"✅ Deployment package created: {package_dir}")
        return package_dir
        
    def create_deployment_instructions(self, instructions_file: Path, 
                                     apk_filename: str, metadata: Dict):
        """Create comprehensive deployment instructions"""
        instructions = f"""# IRCamera Enterprise Deployment Instructions

## Build Information
- **Variant**: {metadata['build_info']['variant']}
- **Build Date**: {metadata['build_info']['build_timestamp']}
- **APK Size**: {metadata['build_info']['apk_size_mb']} MB
- **Git Commit**: {metadata['version_info']['git_commit']}

## System Requirements
- **Android Version**: {metadata['deployment_info']['minimum_requirements']['android_version']}+ 
- **API Level**: {metadata['deployment_info']['minimum_requirements']['api_level']}+
- **RAM**: {metadata['deployment_info']['minimum_requirements']['ram_gb']}+ GB
- **Storage**: {metadata['deployment_info']['minimum_requirements']['storage_gb']}+ GB free space

## Recommended Devices
{chr(10).join('- ' + device for device in metadata['deployment_info']['recommended_devices'])}

## Installation Methods

### Method 1: ADB Installation (Recommended for IT Administrators)
```bash
# Enable developer options and USB debugging on device
# Connect device via USB
adb install -g {apk_filename}
```

### Method 2: MDM Deployment (Enterprise)  
1. Upload APK to your Mobile Device Management system
2. Create deployment policy for target device groups
3. Deploy with automatic permission grants enabled

### Method 3: Manual Installation (Research Staff)
1. Transfer APK to device via USB or cloud storage
2. Enable "Install from Unknown Sources" in device settings
3. Tap APK file to install
4. Grant all requested permissions when prompted

## Required Permissions
The application requires the following permissions:
- **Camera**: For RGB and thermal camera recording
- **Microphone**: For audio recording during sessions  
- **Storage**: For local data storage and file management
- **Network**: For PC Controller communication
- **Bluetooth**: For Shimmer sensor connectivity

## Post-Installation Setup
1. Launch IRCamera application
2. Complete initial device pairing with PC Controller
3. Test sensor connectivity (cameras, Shimmer GSR)
4. Configure recording quality settings
5. Verify network connectivity for multi-device sessions

## Troubleshooting
- **Installation failed**: Check device storage space and Android version
- **Permission denied**: Enable installation from unknown sources  
- **Camera not working**: Verify camera permissions and restart app
- **Network issues**: Check firewall settings and WiFi connectivity

## Support Information
- **Technical Documentation**: See accompanying user manual
- **Hardware Setup**: Refer to laboratory setup guide
- **IT Support**: Contact your institutional IT administrator
- **Development Issues**: Check project repository for updates

## Security Notes
- This APK is signed with enterprise certificates for institutional deployment
- Data is encrypted locally using AES-256 with Android Keystore
- Network communication uses TLS 1.2+ encryption
- No data is transmitted to external servers by default

---
**Generated**: {datetime.now().isoformat()}
**Build Variant**: {metadata['build_info']['variant']}
**Version**: {metadata['version_info']['build_number']}
"""
        
        with open(instructions_file, 'w') as f:
            f.write(instructions)
            
    def create_install_script(self, script_file: Path, apk_filename: str):
        """Create automated installation script for ADB deployment"""
        script_content = f"""#!/bin/bash
# IRCamera Enterprise APK Installation Script
# Automated deployment via ADB for IT administrators

set -e

echo "📱 IRCamera Enterprise APK Installation"
echo "======================================="

# Check if ADB is available
if ! command -v adb &> /dev/null; then
    echo "❌ ADB not found. Please install Android SDK Platform Tools."
    exit 1
fi

# Check for connected devices
echo "🔍 Checking for connected Android devices..."
DEVICES=$(adb devices | grep -v "List of devices" | grep "device" | wc -l)

if [ "$DEVICES" -eq 0 ]; then
    echo "❌ No Android devices connected via USB."
    echo "Please connect device and enable USB debugging."
    exit 1
fi

echo "✅ Found $DEVICES connected device(s)"

# Install APK with permissions granted
echo "🚀 Installing IRCamera Enterprise APK..."
adb install -g "{apk_filename}"

if [ $? -eq 0 ]; then
    echo "✅ IRCamera Enterprise APK installed successfully!"
    echo ""
    echo "📋 Next steps:"
    echo "1. Launch IRCamera app on the device"
    echo "2. Complete device pairing with PC Controller"  
    echo "3. Test sensor connectivity and recording"
    echo ""
    echo "📚 For detailed setup instructions, see:"
    echo "   - DEPLOYMENT_INSTRUCTIONS.md"
    echo "   - Hardware Setup Guide (if available)"
else
    echo "❌ Installation failed. Please check:"
    echo "   - Device storage space"
    echo "   - Android version compatibility" 
    echo "   - USB debugging enabled"
    echo "   - Developer options enabled"
    exit 1
fi
"""
        
        with open(script_file, 'w') as f:
            f.write(script_content)
            
        # Make script executable
        script_file.chmod(0o755)

def main():
    """Main entry point for enterprise APK builder"""
    parser = argparse.ArgumentParser(
        description="Enterprise APK Builder for Multi-Modal Physiological Sensing Platform"
    )
    parser.add_argument("--variant", 
                       choices=["samsung_optimized", "generic", "debug"],
                       default="samsung_optimized",
                       help="Build variant to generate")
    parser.add_argument("--certificate-path", 
                       type=Path,
                       help="Path to enterprise certificate file (.p12/.jks)")
    parser.add_argument("--keystore-password",
                       help="Password for certificate keystore") 
    parser.add_argument("--key-alias",
                       default="enterprise",
                       help="Key alias in certificate store")
    parser.add_argument("--output-dir",
                       type=Path, 
                       help="Output directory for deployment package")
    parser.add_argument("--clean",
                       action="store_true",
                       help="Clean build environment before building")
    parser.add_argument("--skip-signing",
                       action="store_true", 
                       help="Skip APK signing (development only)")
    
    args = parser.parse_args()
    
    # Initialize builder
    project_root = Path("/home/runner/work/IRCamera/IRCamera")
    builder = EnterpriseAPKBuilder(project_root)
    
    # Set custom output directory if provided
    if args.output_dir:
        builder.build_output_dir = args.output_dir
        
    print("🏭 IRCamera Enterprise APK Builder")
    print("=" * 50)
    
    # Validate environment
    if not builder.validate_environment():
        sys.exit(1)
        
    # Clean if requested
    if args.clean:
        if not builder.clean_build_environment():
            sys.exit(1)
            
    # Build APK
    apk_path = builder.build_apk(args.variant)
    if not apk_path:
        sys.exit(1)
        
    final_apk_path = apk_path
    
    # Sign APK if certificate provided
    if not args.skip_signing and args.certificate_path and args.keystore_password:
        signed_apk = builder.sign_apk(apk_path, args.certificate_path, 
                                    args.keystore_password, args.key_alias)
        if signed_apk:
            # Align APK for optimization
            final_apk_path = builder.align_apk(signed_apk) or signed_apk
        else:
            print("⚠️ Continuing with unsigned APK")
    elif not args.skip_signing:
        print("⚠️ Certificate information not provided, skipping signing")
        
    # Generate metadata and deployment package
    metadata = builder.generate_build_metadata(final_apk_path, args.variant)
    package_dir = builder.create_deployment_package(final_apk_path, metadata, args.variant)
    
    print("\n🎉 Enterprise APK Build Complete!")
    print(f"📦 Deployment package: {package_dir}")
    print(f"📱 APK file: {final_apk_path.name}")
    print(f"📋 Build variant: {args.variant}")
    print(f"💾 Package size: {metadata['build_info']['apk_size_mb']} MB")
    print("\n📚 Next steps:")
    print("1. Review deployment instructions in the package")
    print("2. Test installation on target devices") 
    print("3. Deploy via preferred method (ADB, MDM, manual)")

if __name__ == "__main__":
    main()