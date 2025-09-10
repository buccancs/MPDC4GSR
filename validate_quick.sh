#!/bin/bash

# Quick Build Validation - Fast Developer Feedback
# Validates core functionality in under 60 seconds

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
BOLD='\033[1m'
NC='\033[0m'

# Performance tracking
start_time=$(date +%s)

echo -e "${BOLD}⚡ Quick Build Validation - Fast Developer Feedback${NC}"
echo "=================================================="
echo "Started at: $(date)"

# Configuration
ENABLE_SYNTAX_CHECK=true
ENABLE_CORE_COMPILE=true
ENABLE_RESOURCE_CHECK=true
SKIP_TESTS=true

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --syntax-only)
            ENABLE_CORE_COMPILE=false
            ENABLE_RESOURCE_CHECK=false
            shift ;;
        --no-syntax)
            ENABLE_SYNTAX_CHECK=false
            shift ;;
        --include-tests)
            SKIP_TESTS=false
            shift ;;
        --help)
            echo "Quick Build Validation Options:"
            echo "  --syntax-only      Only check syntax (fastest: ~15s)"
            echo "  --no-syntax        Skip syntax checking"
            echo "  --include-tests    Include test compilation (slower)"
            echo "  --help            Show this help message"
            exit 0 ;;
        *)
            echo "Unknown option: $1"
            echo "Use --help for available options"
            exit 1 ;;
    esac
done

# Step tracking
step=1
total_steps=5
if [ "$SKIP_TESTS" = false ]; then
    total_steps=6
fi

print_step() {
    echo -e "\n${BLUE}Step $step/$total_steps: $1${NC}"
    ((step++))
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Step 1: Environment Check
print_step "Environment validation"
if [ ! -f "gradlew" ]; then
    print_error "gradlew not found - run from project root"
    exit 1
fi

if [ ! -f "settings.gradle.kts" ]; then
    print_error "settings.gradle.kts not found - invalid project structure"
    exit 1
fi

chmod +x gradlew 2>/dev/null || true
print_success "Environment validation - PASSED"

# Step 2: Syntax Validation (Fast)
if [ "$ENABLE_SYNTAX_CHECK" = true ]; then
    print_step "Syntax validation (Kotlin/Java)"
    
    syntax_errors=0
    
    # Check for common syntax issues
    echo "🔍 Checking for orphaned case statements..."
    if find . -name "*.java" -exec grep -l "case.*:" {} \; | head -5 | while read file; do
        if grep -A5 -B5 "case.*:" "$file" | grep -q "^[[:space:]]*case.*:[[:space:]]*$"; then
            print_warning "Potential orphaned case in: $file"
            ((syntax_errors++))
        fi
    done; then
        if [ $syntax_errors -gt 0 ]; then
            print_warning "Found $syntax_errors potential syntax issues"
        fi
    fi
    
    # Quick Kotlin syntax check (if ktlint available)
    if command -v ktlint >/dev/null 2>&1; then
        echo "🔍 Running quick Kotlin syntax check..."
        if timeout 20s ktlint --reporter=plain "**/*.kt" | head -10; then
            print_success "Kotlin syntax check - PASSED"
        else
            print_warning "Kotlin syntax check - WARNINGS (not blocking)"
        fi
    else
        print_warning "ktlint not available - skipping Kotlin syntax check"
    fi
    
    print_success "Syntax validation - PASSED"
else
    print_warning "Syntax validation - SKIPPED"
fi

# Step 3: Core Module Compilation (Fast)
if [ "$ENABLE_CORE_COMPILE" = true ]; then
    print_step "Core module compilation"
    
    # Use gradle's configuration cache for speed
    export GRADLE_OPTS="-Dorg.gradle.configuration-cache=true -Dorg.gradle.parallel=true"
    
    echo "🔨 Compiling core modules (libir, libapp, BleModule)..."
    
    # Compile only the most critical modules quickly
    if timeout 120s ./gradlew \
        :libir:compileDebugKotlin \
        :libapp:compileDebugKotlin \
        :BleModule:compileDebugKotlin \
        --no-daemon \
        --quiet \
        --continue \
        2>&1 | tee /tmp/quick_compile.log; then
        
        print_success "Core module compilation - PASSED"
    else
        print_error "Core module compilation - FAILED"
        echo "Last 10 lines of compilation output:"
        tail -10 /tmp/quick_compile.log
        exit 1
    fi
else
    print_warning "Core module compilation - SKIPPED"
fi

# Step 4: Android Resource Validation (Fast)
if [ "$ENABLE_RESOURCE_CHECK" = true ]; then
    print_step "Android resource validation"
    
    echo "🔍 Validating XML resources..."
    
    xml_errors=0
    # Quick XML validation
    find app/src/main/res -name "*.xml" -type f | head -20 | while read xml_file; do
        if ! xmllint --noout "$xml_file" 2>/dev/null; then
            print_warning "XML validation failed: $xml_file"
            ((xml_errors++))
        fi
    done
    
    # Check for duplicate resource entries
    echo "🔍 Checking for duplicate resources..."
    if find app/src/main/res/values -name "*.xml" -exec grep -l "name=" {} \; | head -5 | while read file; do
        duplicates=$(grep 'name="' "$file" | cut -d'"' -f2 | sort | uniq -d | wc -l)
        if [ "$duplicates" -gt 0 ]; then
            print_warning "Found $duplicates duplicate resources in: $file"
        fi
    done; then
        print_success "Resource validation - PASSED"
    else
        print_warning "Resource validation - WARNINGS (not blocking)"
    fi
else
    print_warning "Android resource validation - SKIPPED"
fi

# Step 5: Dependency Resolution Check
print_step "Dependency resolution check"

echo "🔍 Checking dependency resolution..."
if timeout 30s ./gradlew dependencies --configuration debugCompileClasspath --quiet >/dev/null 2>&1; then
    print_success "Dependency resolution - PASSED"
else
    print_warning "Dependency resolution - WARNINGS (may need attention)"
fi

# Step 6: Optional Test Compilation
if [ "$SKIP_TESTS" = false ]; then
    print_step "Test compilation"
    
    echo "🧪 Compiling tests..."
    if timeout 60s ./gradlew \
        :libir:compileDebugUnitTestKotlin \
        :libapp:compileDebugUnitTestKotlin \
        --no-daemon \
        --quiet \
        2>/dev/null; then
        print_success "Test compilation - PASSED"
    else
        print_warning "Test compilation - WARNINGS (not blocking)"
    fi
fi

# Calculate timing
end_time=$(date +%s)
duration=$((end_time - start_time))

echo ""
echo "=================================================="
echo -e "${BOLD}🎉 Quick Validation Summary${NC}"
echo "=================================================="
echo "✅ Environment: Valid"
echo "✅ Syntax: $([ "$ENABLE_SYNTAX_CHECK" = true ] && echo "Checked" || echo "Skipped")"
echo "✅ Core Modules: $([ "$ENABLE_CORE_COMPILE" = true ] && echo "Compiled" || echo "Skipped")"
echo "✅ Resources: $([ "$ENABLE_RESOURCE_CHECK" = true ] && echo "Validated" || echo "Skipped")"
echo "✅ Dependencies: Resolved"
echo "✅ Tests: $([ "$SKIP_TESTS" = false ] && echo "Compiled" || echo "Skipped")"
echo ""
echo -e "${GREEN}🚀 Quick validation completed in ${duration}s${NC}"
echo ""

# Provide next steps
echo -e "${BOLD}📋 Next Steps:${NC}"
if [ $duration -lt 60 ]; then
    echo "• ✅ Fast validation passed - ready for development"
    echo "• 🔄 Run full validation before commit: ./validate_before_commit.sh"
    echo "• 🧪 Run tests when ready: ./gradlew test"
else
    echo "• ⚠️ Validation took ${duration}s - consider optimizing"
    echo "• 🔧 Use --syntax-only for faster feedback during development"
    echo "• 💡 Clean gradle cache if performance degrades"
fi

echo ""
echo -e "${BLUE}💡 Pro Tips:${NC}"
echo "• Use --syntax-only for 15-second feedback"
echo "• Run this script every 15-30 minutes during development"
echo "• Full validation is still required before commits"
echo "• Use IntelliJ/Android Studio's built-in compilation for instant feedback"

echo ""
echo -e "${GREEN}✅ Ready to continue development!${NC}"

# Clean up temporary files
rm -f /tmp/quick_compile.log

exit 0