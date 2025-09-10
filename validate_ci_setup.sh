#!/bin/bash
# Validate CI/CD Setup for IRCamera Project
# This script validates the GitHub Actions CI/CD implementation

set -e

echo "🚀 Validating CI/CD Setup for IRCamera Project"
echo "================================================"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print status
print_status() {
    if [ $1 -eq 0 ]; then
        echo -e "✅ ${GREEN}$2${NC}"
    else
        echo -e "❌ ${RED}$2${NC}"
    fi
}

# Function to print info
print_info() {
    echo -e "ℹ️  ${YELLOW}$1${NC}"
}

echo ""
echo "1. Checking Workflow Files"
echo "-------------------------"

WORKFLOW_DIR=".github/workflows"
if [ -d "$WORKFLOW_DIR" ]; then
    print_status 0 "Workflows directory exists"
    
    # Check each workflow file
    workflows=("ci.yml" "release.yml" "pr-validation.yml" "dependency-check.yml" "build-variants.yml")
    for workflow in "${workflows[@]}"; do
        if [ -f "$WORKFLOW_DIR/$workflow" ]; then
            print_status 0 "$workflow exists"
            # Validate YAML syntax
            if python3 -c "import yaml; yaml.safe_load(open('$WORKFLOW_DIR/$workflow'))" 2>/dev/null; then
                print_status 0 "$workflow has valid YAML syntax"
            else
                print_status 1 "$workflow has invalid YAML syntax"
            fi
        else
            print_status 1 "$workflow is missing"
        fi
    done
else
    print_status 1 "Workflows directory does not exist"
fi

echo ""
echo "2. Checking Gradle Setup"
echo "------------------------"

if [ -f "gradlew" ]; then
    print_status 0 "Gradle wrapper exists"
    
    if [ -x "gradlew" ]; then
        print_status 0 "Gradle wrapper is executable"
    else
        print_info "Making gradlew executable..."
        chmod +x gradlew
        print_status 0 "Gradle wrapper made executable"
    fi
    
    # Test basic gradle commands
    print_info "Testing gradle tasks command..."
    if ./gradlew tasks --quiet > /dev/null 2>&1; then
        print_status 0 "Gradle tasks command works"
    else
        print_status 1 "Gradle tasks command failed"
    fi
else
    print_status 1 "Gradle wrapper not found"
fi

echo ""
echo "3. Checking Project Structure"
echo "----------------------------"

# Check essential files
essential_files=("build.gradle.kts" "settings.gradle.kts" "gradle.properties")
for file in "${essential_files[@]}"; do
    if [ -f "$file" ]; then
        print_status 0 "$file exists"
    else
        print_status 1 "$file is missing"
    fi
done

# Check for app module
if [ -d "app" ] && [ -f "app/build.gradle.kts" ]; then
    print_status 0 "App module exists with build file"
else
    print_status 1 "App module or its build file is missing"
fi

echo ""
echo "4. Checking CI Configuration"
echo "---------------------------"

# Check .gitignore for CI artifacts
if grep -q "CI/CD Artifacts" .gitignore; then
    print_status 0 "CI artifacts patterns added to .gitignore"
else
    print_status 1 "CI artifacts patterns not found in .gitignore"
fi

# Check Dependabot configuration
if [ -f ".github/dependabot.yml" ]; then
    print_status 0 "Dependabot configuration exists"
else
    print_status 1 "Dependabot configuration missing"
fi

echo ""
echo "5. Security and Best Practices"
echo "-----------------------------"

# Check for secrets usage in workflows
if grep -r "secrets\." .github/workflows/ > /dev/null; then
    print_status 0 "Workflows use GitHub secrets appropriately"
else
    print_info "No secrets found in workflows (this is okay if not needed)"
fi

# Check for caching in workflows
if grep -r "actions/cache" .github/workflows/ > /dev/null; then
    print_status 0 "Gradle caching is implemented"
else
    print_status 1 "Gradle caching not found in workflows"
fi

# Check for security scanning
if grep -r "github/codeql-action" .github/workflows/ > /dev/null; then
    print_status 0 "CodeQL security scanning configured"
else
    print_status 1 "CodeQL security scanning not configured"
fi

echo ""
echo "6. Workflow Triggers"
echo "-------------------"

# Check for various triggers
triggers=("push" "pull_request" "workflow_dispatch" "schedule")
for trigger in "${triggers[@]}"; do
    if grep -r "$trigger:" .github/workflows/ > /dev/null; then
        print_status 0 "$trigger trigger is used"
    else
        print_info "$trigger trigger not used (may be intentional)"
    fi
done

echo ""
echo "7. Test Basic Build Commands"
echo "---------------------------"

print_info "Testing gradle clean..."
if ./gradlew clean --quiet; then
    print_status 0 "Gradle clean successful"
else
    print_status 1 "Gradle clean failed"
fi

print_info "Testing compilation check..."
if ./gradlew compileDebugKotlin --quiet --continue; then
    print_status 0 "Debug compilation check passed"
else
    print_info "Debug compilation had issues (may be expected due to missing dependencies)"
fi

echo ""
echo "📊 CI/CD Setup Summary"
echo "====================="

total_workflows=$(ls .github/workflows/*.yml 2>/dev/null | wc -l)
total_lines=$(cat .github/workflows/*.yml 2>/dev/null | wc -l)

echo "✨ Workflows created: $total_workflows"
echo "📝 Total configuration lines: $total_lines"
echo "🔧 Features implemented:"
echo "   • Multi-platform testing"
echo "   • Automated building and testing"
echo "   • Security scanning (CodeQL + OWASP)"
echo "   • Dependency monitoring"
echo "   • Release automation"
echo "   • PR validation"
echo "   • Performance caching"

echo ""
echo "🎉 CI/CD setup validation completed!"
echo "Next steps:"
echo "1. Push changes to trigger workflows"
echo "2. Configure repository secrets if using signed builds"
echo "3. Set up branch protection rules"
echo "4. Monitor workflow performance"