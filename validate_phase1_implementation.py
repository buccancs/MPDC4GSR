#!/usr/bin/env python3
"""
Phase 1 Implementation Validation Script

This script validates that all Phase 1 requirements for permissions handling
have been properly implemented in the Multi-Modal Physiological Sensing Platform.
"""

import os
import re
import sys
from pathlib import Path

def check_file_exists(filepath, description):
    """Check if a file exists and return validation result."""
    exists = Path(filepath).exists()
    status = "✅" if exists else "❌"
    print(f"{status} {description}: {filepath}")
    return exists

def check_content_in_file(filepath, pattern, description):
    """Check if specific content pattern exists in file."""
    if not Path(filepath).exists():
        print(f"❌ {description}: File not found - {filepath}")
        return False
    
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
            found = re.search(pattern, content, re.MULTILINE | re.DOTALL)
            status = "✅" if found else "❌"
            print(f"{status} {description}")
            return bool(found)
    except Exception as e:
        print(f"❌ {description}: Error reading file - {e}")
        return False

def validate_phase1_implementation():
    """Validate Phase 1 permissions handling implementation."""
    
    print("🔍 Phase 1 Permissions Handling Implementation Validation")
    print("=" * 60)
    
    base_path = Path(__file__).parent
    app_path = base_path / "app" / "src" / "main" / "java" / "com" / "topdon" / "tc001"
    
    # Check core files exist
    print("\n📁 Core Files Structure:")
    files_exist = []
    
    # PermissionController
    files_exist.append(check_file_exists(
        app_path / "permissions" / "PermissionController.kt",
        "PermissionController class"
    ))
    
    # PermissionUtils
    files_exist.append(check_file_exists(
        app_path / "permissions" / "PermissionUtils.kt",
        "PermissionUtils helper class"
    ))
    
    # Updated MultiModalRecordingActivity
    files_exist.append(check_file_exists(
        app_path / "gsr" / "MultiModalRecordingActivity.kt",
        "Updated MultiModalRecordingActivity"
    ))
    
    # AndroidManifest.xml
    manifest_path = base_path / "app" / "src" / "main" / "AndroidManifest.xml"
    files_exist.append(check_file_exists(manifest_path, "AndroidManifest.xml"))
    
    # Check Permission Controller Implementation
    print("\n🔐 PermissionController Implementation:")
    controller_path = app_path / "permissions" / "PermissionController.kt"
    
    permission_checks = []
    
    # Check for comprehensive permission arrays
    permission_checks.append(check_content_in_file(
        controller_path,
        r"CAMERA_PERMISSIONS.*=.*arrayOf",
        "Camera permissions array defined"
    ))
    
    permission_checks.append(check_content_in_file(
        controller_path,
        r"BLUETOOTH_PERMISSIONS_ANDROID_12.*=.*arrayOf",
        "Android 12+ Bluetooth permissions defined"
    ))
    
    permission_checks.append(check_content_in_file(
        controller_path,
        r"STORAGE_PERMISSIONS_ANDROID_13.*=.*arrayOf",
        "Android 13+ storage permissions defined"
    ))
    
    permission_checks.append(check_content_in_file(
        controller_path,
        r"NOTIFICATION_PERMISSIONS.*=.*if.*Build\.VERSION\.SDK_INT.*TIRAMISU",
        "Android 13+ notification permissions with version check"
    ))
    
    # Check for key methods
    permission_checks.append(check_content_in_file(
        controller_path,
        r"fun requestAllPermissions",
        "requestAllPermissions method"
    ))
    
    permission_checks.append(check_content_in_file(
        controller_path,
        r"fun requestUsbPermission",
        "requestUsbPermission method for thermal cameras"
    ))
    
    permission_checks.append(check_content_in_file(
        controller_path,
        r"fun requestBatteryOptimizationExemption",
        "requestBatteryOptimizationExemption method"
    ))
    
    permission_checks.append(check_content_in_file(
        controller_path,
        r"fun getMissingPermissions",
        "getMissingPermissions analysis method"
    ))
    
    # Check Manifest Updates
    print("\n📋 AndroidManifest.xml Updates:")
    manifest_checks = []
    
    manifest_checks.append(check_content_in_file(
        manifest_path,
        r"POST_NOTIFICATIONS",
        "POST_NOTIFICATIONS permission for Android 13+"
    ))
    
    manifest_checks.append(check_content_in_file(
        manifest_path,
        r"REQUEST_IGNORE_BATTERY_OPTIMIZATIONS",
        "Battery optimization exemption permission"
    ))
    
    manifest_checks.append(check_content_in_file(
        manifest_path,
        r"ACCESS_FINE_LOCATION",
        "Location permissions for BLE scanning"
    ))
    
    manifest_checks.append(check_content_in_file(
        manifest_path,
        r"BLUETOOTH_SCAN",
        "Android 12+ Bluetooth scan permission"
    ))
    
    manifest_checks.append(check_content_in_file(
        manifest_path,
        r"FOREGROUND_SERVICE_CAMERA",
        "Foreground service camera permission"
    ))
    
    # Check Activity Integration
    print("\n🎯 Activity Integration:")
    activity_path = app_path / "gsr" / "MultiModalRecordingActivity.kt"
    integration_checks = []
    
    integration_checks.append(check_content_in_file(
        activity_path,
        r"import.*PermissionController",
        "PermissionController import"
    ))
    
    integration_checks.append(check_content_in_file(
        activity_path,
        r"private lateinit var permissionController: PermissionController",
        "PermissionController property declaration"
    ))
    
    integration_checks.append(check_content_in_file(
        activity_path,
        r"permissionController\.initialize\(\)",
        "PermissionController initialization"
    ))
    
    integration_checks.append(check_content_in_file(
        activity_path,
        r"permissionController\.hasAllRequiredPermissions\(\)",
        "Permission checking integration"
    ))
    
    integration_checks.append(check_content_in_file(
        activity_path,
        r"permissionController\.requestBatteryOptimizationExemption",
        "Battery optimization integration"
    ))
    
    # Check Documentation
    print("\n📚 Documentation:")
    doc_checks = []
    
    readme_path = app_path / "permissions" / "README.md"
    doc_checks.append(check_file_exists(readme_path, "Permissions README documentation"))
    
    if readme_path.exists():
        doc_checks.append(check_content_in_file(
            readme_path,
            r"Phase 1.*Permissions Handling",
            "Phase 1 documentation title"
        ))
        
        doc_checks.append(check_content_in_file(
            readme_path,
            r"Usage Example",
            "Usage example documentation"
        ))
    
    # Calculate results
    print("\n📊 Validation Summary:")
    total_files = len(files_exist)
    passed_files = sum(files_exist)
    
    total_permissions = len(permission_checks)
    passed_permissions = sum(permission_checks)
    
    total_manifest = len(manifest_checks)
    passed_manifest = sum(manifest_checks)
    
    total_integration = len(integration_checks)
    passed_integration = sum(integration_checks)
    
    total_docs = len(doc_checks)
    passed_docs = sum(doc_checks)
    
    print(f"📁 File Structure: {passed_files}/{total_files}")
    print(f"🔐 Permission Logic: {passed_permissions}/{total_permissions}")
    print(f"📋 Manifest Updates: {passed_manifest}/{total_manifest}")
    print(f"🎯 Activity Integration: {passed_integration}/{total_integration}")
    print(f"📚 Documentation: {passed_docs}/{total_docs}")
    
    total_checks = total_files + total_permissions + total_manifest + total_integration + total_docs
    passed_checks = passed_files + passed_permissions + passed_manifest + passed_integration + passed_docs
    
    print(f"\n🎯 Overall Score: {passed_checks}/{total_checks} ({passed_checks/total_checks*100:.1f}%)")
    
    if passed_checks == total_checks:
        print("\n🎉 SUCCESS: Phase 1 permissions handling implementation is COMPLETE!")
        return True
    else:
        print(f"\n⚠️  PARTIAL: {total_checks - passed_checks} items need attention")
        return False

if __name__ == "__main__":
    success = validate_phase1_implementation()
    sys.exit(0 if success else 1)