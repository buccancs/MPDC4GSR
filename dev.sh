#!/bin/bash

# Simple Development Tools
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
    echo "  format    - Format all code files"
    echo "  lint      - Run linting checks"
    echo "  build     - Build the project"
    echo "  test      - Run tests"
    echo "  validate  - Run all checks (format + lint + build)"
    echo "  clean     - Clean build artifacts"
    echo "  help      - Show this help"
}

print_status() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

format_code() {
    print_status "Formatting code..."
    
    # Format Kotlin files
    if command -v ktlint &> /dev/null; then
        find . -name "*.kt" -not -path "./build/*" -not -path "./.gradle/*" | xargs ktlint --format || true
    fi
    
    # Format Java files
    if command -v google-java-format &> /dev/null; then
        find . -name "*.java" -not -path "./build/*" -not -path "./.gradle/*" | xargs google-java-format --replace || true
    fi
    
    # Format Python files
    if command -v black &> /dev/null; then
        find . -name "*.py" | xargs black --quiet || true
    fi
    
    # Format XML files
    find . -name "*.xml" -not -path "./build/*" -not -path "./.gradle/*" | while read -r file; do
        if command -v xmllint &> /dev/null; then
            xmllint --format "$file" --output "$file" 2>/dev/null || true
        fi
    done
    
    print_status "Code formatting completed"
}

run_lint() {
    print_status "Running linting checks..."
    
    local errors=0
    
    # Kotlin lint
    if command -v ktlint &> /dev/null; then
        if ! find . -name "*.kt" -not -path "./build/*" | xargs ktlint --quiet; then
            ((errors++))
        fi
    fi
    
    # Python lint
    if command -v flake8 &> /dev/null; then
        if ! find . -name "*.py" | xargs flake8 --quiet; then
            ((errors++))
        fi
    fi
    
    # Shell script lint
    if command -v shellcheck &> /dev/null; then
        if ! find . -name "*.sh" | xargs shellcheck; then
            ((errors++))
        fi
    fi
    
    if [ $errors -eq 0 ]; then
        print_status "All linting checks passed"
    else
        print_error "Found $errors linting issues"
        return 1
    fi
}

build_project() {
    print_status "Building project..."
    
    if [ -f "./gradlew" ]; then
        ./gradlew :app:assemble
        print_status "Build completed successfully"
    else
        print_error "No gradlew found"
        return 1
    fi
}

run_tests() {
    print_status "Running tests..."
    
    if [ -f "./gradlew" ]; then
        ./gradlew :app:testDebugUnitTest
        print_status "Tests completed"
    else
        print_error "No gradlew found"
        return 1
    fi
}

validate_all() {
    print_status "Running full validation..."
    
    format_code
    run_lint
    build_project
    
    print_status "All validation checks passed!"
}

clean_artifacts() {
    print_status "Cleaning build artifacts..."
    
    if [ -f "./gradlew" ]; then
        ./gradlew clean
    fi
    
    rm -rf build/
    rm -rf .gradle/
    find . -name "*.log" -delete
    find . -name "*.tmp" -delete
    
    print_status "Clean completed"
}

# Main command handler
case "${1:-help}" in
    "format")
        format_code
        ;;
    "lint")
        run_lint
        ;;
    "build")
        build_project
        ;;
    "test")
        run_tests
        ;;
    "validate")
        validate_all
        ;;
    "clean")
        clean_artifacts
        ;;
    "help"|*)
        show_help
        ;;
esac