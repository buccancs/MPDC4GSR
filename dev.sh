#!/bin/bash

# IRCamera Development Tools - CI/CD Core
# Usage: ./dev.sh [command]

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

show_help() {
    echo -e "${BLUE}IRCamera Development Tools${NC}"
    echo ""
    echo "Usage: ./dev.sh [command]"
    echo ""
    echo "Commands:"
    echo "  lint        - Run linting checks (ktlint, checkstyle)"
    echo "  static      - Run static analysis"
    echo "  build-check - Quick build validation"
    echo "  validate    - Run all checks (lint + static + build)"
    echo "  help        - Show this help"
}

print_status() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

run_lint() {
    print_status "Running linting checks..."
    
    local errors=0
    
    # Kotlin lint with ktlint
    if command -v ktlint &> /dev/null; then
        print_status "Running ktlint on Kotlin files..."
        if ! find . -name "*.kt" -not -path "./build/*" -not -path "./.gradle/*" -exec ktlint {} + 2>/dev/null; then
            errors=$((errors + 1))
            print_error "Kotlin linting issues found"
        else
            print_status "Kotlin linting passed"
        fi
    else
        print_warning "ktlint not available. Install with: brew install ktlint (macOS) or download from GitHub"
    fi
    
    # Python lint with flake8
    if command -v flake8 &> /dev/null; then
        print_status "Running flake8 on Python files..."
        if ! find . -name "*.py" -exec flake8 {} + 2>/dev/null; then
            errors=$((errors + 1))
            print_error "Python flake8 issues found"
        else
            print_status "Python linting passed"
        fi
    fi
    
    if [ $errors -eq 0 ]; then
        print_status "All linting checks passed"
        return 0
    else
        print_error "Linting failed with $errors error(s)"
        return 1
    fi
}

run_static_analysis() {
    print_status "Running static analysis..."
    
    # Basic static analysis - can be expanded
    local issues=0
    
    # Check for common issues in Kotlin/Java files
    if command -v grep &> /dev/null; then
        print_status "Checking for common code issues..."
        
        # Check for potential null pointer issues
        if find . -name "*.kt" -o -name "*.java" | xargs grep -l "!!" 2>/dev/null | grep -v build > /dev/null; then
            print_warning "Found potential null assertion issues (!!)"
            issues=$((issues + 1))
        fi
        
        # Check for hardcoded strings
        if find . -name "*.kt" -o -name "*.java" | xargs grep -l "Log\." 2>/dev/null | grep -v build > /dev/null; then
            print_warning "Found hardcoded logging statements"
            issues=$((issues + 1))
        fi
    fi
    
    if [ $issues -eq 0 ]; then
        print_status "Static analysis passed"
        return 0
    else
        print_warning "Static analysis completed with $issues warning(s)"
        return 0  # warnings don't fail the build
    fi
}

run_build_check() {
    print_status "Running build validation..."
    
    # Check if gradlew exists and is executable
    if [ ! -f "./gradlew" ]; then
        print_error "gradlew not found"
        return 1
    fi
    
    if [ ! -x "./gradlew" ]; then
        chmod +x ./gradlew
        print_status "Made gradlew executable"
    fi
    
    # Run gradle check
    if ./gradlew assemble --no-daemon --console=plain; then
        print_status "Build validation passed"
        return 0
    else
        print_error "Build validation failed"
        return 1
    fi
}

run_validate() {
    print_status "Running full validation..."
    
    local overall_status=0
    
    # Run linting
    if ! run_lint; then
        overall_status=1
    fi
    
    # Run static analysis
    run_static_analysis  # doesn't fail on warnings
    
    # Run build check
    if ! run_build_check; then
        overall_status=1
    fi
    
    if [ $overall_status -eq 0 ]; then
        print_status "All validation checks passed ✨"
    else
        print_error "Some validation checks failed"
    fi
    
    return $overall_status
}

# Main script logic
case "${1:-help}" in
    lint)
        run_lint
        ;;
    static)
        run_static_analysis
        ;;
    build-check)
        run_build_check
        ;;
    validate)
        run_validate
        ;;
    help|*)
        show_help
        ;;
esac