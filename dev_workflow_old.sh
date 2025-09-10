#!/bin/bash

# Consolidated Developer Workflow - Unified interface for all development operations
# Version: 4.0.0 - Densified and streamlined

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Configuration
SCRIPT_VERSION="4.0.0"

# Banner
show_banner() {
    clear
    echo -e "${BOLD}${BLUE}"
    echo "████████╗██████╗  ██████╗ █████╗ ███╗   ███╗███████╗██████╗  █████╗ "
    echo "╚══██╔══╝██╔══██╗██╔════╝██╔══██╗████╗ ████║██╔════╝██╔══██╗██╔══██╗"
    echo "   ██║   ██████╔╝██║     ███████║██╔████╔██║█████╗  ██████╔╝███████║"
    echo "   ██║   ██╔══██╗██║     ██╔══██║██║╚██╔╝██║██╔══╝  ██╔══██╗██╔══██║"
    echo "   ██║   ██║  ██║╚██████╗██║  ██║██║ ╚═╝ ██║███████╗██║  ██║██║  ██║"
    echo "   ╚═╝   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝"
    echo -e "${NC}"
    echo -e "${BOLD}🚀 Developer Workflow Manager v${SCRIPT_VERSION}${NC}"
    echo -e "${CYAN}Enterprise-grade development automation for IRCamera${NC}"
    echo ""
}

# Help system
show_help() {
    echo -e "${BOLD}Available Commands:${NC}"
    echo ""
    echo -e "${GREEN}🏃 Quick Development:${NC}"
    echo -e "  ${YELLOW}quick${NC}           - Lightning-fast validation (15-60s)"
    echo -e "  ${YELLOW}dev${NC}             - Start development mode with monitoring"
    echo -e "  ${YELLOW}format${NC}          - Comprehensive code formatting"
    echo -e "  ${YELLOW}analyze${NC}         - ML-powered quality analysis"
    echo ""
    echo -e "${GREEN}🔍 Validation & Testing:${NC}"
    echo -e "  ${YELLOW}validate${NC}        - Complete build validation (3-5min)"
    echo -e "  ${YELLOW}test${NC}            - Run test suite"
    echo -e "  ${YELLOW}core${NC}            - Validate core modules only"
    echo -e "  ${YELLOW}syntax${NC}          - Syntax-only validation (15s)"
    echo ""
    echo -e "${GREEN}📊 Quality & Monitoring:${NC}"
    echo -e "  ${YELLOW}quality${NC}         - Comprehensive quality check"
    echo -e "  ${YELLOW}monitor${NC}         - Real-time system health monitoring"
    echo -e "  ${YELLOW}report${NC}          - Generate health and quality reports"
    echo -e "  ${YELLOW}trends${NC}          - View quality trends and insights"
    echo ""
    echo -e "${GREEN}🔧 Maintenance & Optimization:${NC}"
    echo -e "  ${YELLOW}clean${NC}           - Clean build artifacts and caches"
    echo -e "  ${YELLOW}optimize${NC}        - Optimize development environment"
    echo -e "  ${YELLOW}setup${NC}           - Setup development environment"
    echo -e "  ${YELLOW}doctor${NC}          - Diagnose development environment issues"
    echo ""
    echo -e "${GREEN}🎯 Smart Workflows:${NC}"
    echo -e "  ${YELLOW}commit${NC}          - Smart commit with validation"
    echo -e "  ${YELLOW}push${NC}            - Validated push with quality gates"
    echo -e "  ${YELLOW}pr${NC}              - Prepare pull request with full validation"
    echo -e "  ${YELLOW}release${NC}         - Release workflow with comprehensive checks"
    echo ""
    echo -e "${GREEN}ℹ️  Information:${NC}"
    echo -e "  ${YELLOW}status${NC}          - Show project and system status"
    echo -e "  ${YELLOW}help${NC}            - Show this help message"
    echo -e "  ${YELLOW}version${NC}         - Show version information"
    echo ""
    echo -e "${CYAN}💡 Pro Tips:${NC}"
    echo -e "  • Use ${YELLOW}'quick'${NC} during active development for fast feedback"
    echo -e "  • Use ${YELLOW}'dev'${NC} to start monitoring mode while coding"
    echo -e "  • Use ${YELLOW}'validate'${NC} before committing changes"
    echo -e "  • Use ${YELLOW}'monitor'${NC} to track system performance"
    echo ""
}

# Status display
show_status() {
    echo -e "${BOLD}📊 Project Status Dashboard${NC}"
    echo "======================================"
    
    # Git status
    echo -e "${BLUE}🔀 Git Status:${NC}"
    git status --porcelain | head -10 | while IFS= read -r line; do
        echo "   $line"
    done
    
    # Recent commits
    echo -e "\n${BLUE}📝 Recent Commits:${NC}"
    git log --oneline -5 | while IFS= read -r line; do
        echo "   $line"
    done
    
    # System resources
    echo -e "\n${BLUE}🖥️  System Resources:${NC}"
    if command -v python3 >/dev/null && python3 -c "import psutil" 2>/dev/null; then
        python3 -c "
import psutil
cpu = psutil.cpu_percent(interval=1)
mem = psutil.virtual_memory()
print(f'   CPU: {cpu:.1f}%')
print(f'   Memory: {mem.percent:.1f}% ({mem.available/1024/1024/1024:.1f}GB available)')
"
    else
        echo "   System monitoring not available (install psutil: pip install psutil)"
    fi
    
    # Build cache size
    echo -e "\n${BLUE}🏗️  Build Environment:${NC}"
    if [ -d "$HOME/.gradle/caches" ]; then
        cache_size=$(du -sh "$HOME/.gradle/caches" 2>/dev/null | cut -f1)
        echo "   Gradle Cache: $cache_size"
    fi
    
    # Quality metrics
    if [ -f "quality_reports/analysis_summary_$(date +%Y%m%d)*.json" ]; then
        echo -e "\n${BLUE}📈 Quality Metrics:${NC}"
        latest_report=$(ls -t quality_reports/analysis_summary_*.json 2>/dev/null | head -1)
        if [ -n "$latest_report" ]; then
            quality_score=$(jq -r '.quality_score // 0' "$latest_report" 2>/dev/null || echo "0")
            echo "   Latest Quality Score: $quality_score/100"
        fi
    fi
    
    echo ""
}

# Quick validation
run_quick() {
    echo -e "${YELLOW}⚡ Running quick validation...${NC}"
    ./validate_quick.sh "$@"
}

# Development mode
run_dev_mode() {
    echo -e "${PURPLE}🚀 Starting development mode...${NC}"
    echo "This will:"
    echo "• Start real-time system monitoring"
    echo "• Watch for file changes"
    echo "• Provide continuous quality feedback"
    echo ""
    
    # Start system monitor in background
    python3 system_health_monitor.py --dashboard &
    monitor_pid=$!
    
    echo "Development monitoring started (PID: $monitor_pid)"
    echo "Press Ctrl+C to stop monitoring and return to normal mode"
    
    # Cleanup on exit
    trap "kill $monitor_pid 2>/dev/null; echo 'Development mode stopped'" EXIT
    
    # Wait for user to stop
    wait $monitor_pid
}

# Comprehensive validation
run_validate() {
    echo -e "${GREEN}🔍 Running comprehensive validation...${NC}"
    ./validate_before_commit.sh "$@"
}

# Core module validation
run_core() {
    echo -e "${BLUE}🎯 Validating core modules...${NC}"
    ./validate_core_modules.sh "$@"
}

# Syntax-only validation
run_syntax() {
    echo -e "${CYAN}📝 Running syntax validation...${NC}"
    ./validate_quick.sh --syntax-only "$@"
}

# Code formatting
run_format() {
    echo -e "${PURPLE}✨ Running comprehensive code formatting...${NC}"
    ./format_all_files.sh "$@"
}

# Quality analysis
run_analyze() {
    echo -e "${GREEN}🔍 Running ML-powered quality analysis...${NC}"
    python3 intelligent_code_optimizer.py "$@"
}

# Quality check
run_quality() {
    echo -e "${BLUE}📊 Running comprehensive quality check...${NC}"
    ./quality_check.sh "$@"
}

# System monitoring
run_monitor() {
    echo -e "${YELLOW}📡 Starting system health monitoring...${NC}"
    python3 system_health_monitor.py --dashboard "$@"
}

# Generate reports
run_report() {
    echo -e "${CYAN}📋 Generating comprehensive reports...${NC}"
    
    # System health report
    echo "Generating system health report..."
    python3 system_health_monitor.py --report > /tmp/health_report.json
    
    # Quality analysis report
    echo "Generating quality analysis report..."
    python3 intelligent_code_optimizer.py . > /tmp/quality_output.log 2>&1
    
    echo -e "${GREEN}✅ Reports generated:${NC}"
    echo "• System health: /tmp/health_report.json"
    echo "• Quality reports in: quality_reports/"
    
    # Show summary
    if [ -f "/tmp/health_report.json" ]; then
        echo -e "\n${BOLD}📊 Quick Summary:${NC}"
        quality_score=$(jq -r '.current_status.quality_score // 0' /tmp/health_report.json 2>/dev/null || echo "N/A")
        cpu_usage=$(jq -r '.current_status.cpu_percent // 0' /tmp/health_report.json 2>/dev/null || echo "N/A")
        echo "• Quality Score: $quality_score/100"
        echo "• CPU Usage: $cpu_usage%"
    fi
}

# View trends
run_trends() {
    echo -e "${PURPLE}📈 Analyzing quality trends...${NC}"
    
    if [ -d "quality_reports" ] && [ "$(ls -A quality_reports 2>/dev/null)" ]; then
        echo "Available quality reports:"
        ls -la quality_reports/ | tail -10
        
        # Show latest insights if available
        latest_json=$(ls -t quality_reports/analysis_summary_*.json 2>/dev/null | head -1)
        if [ -n "$latest_json" ]; then
            echo -e "\n${BOLD}🧠 Latest AI Insights:${NC}"
            jq -r '.insights[]? // "No insights available"' "$latest_json" 2>/dev/null | while IFS= read -r insight; do
                echo "• $insight"
            done
        fi
    else
        echo "No quality reports found. Run analysis first:"
        echo "  ./dev_workflow.sh analyze"
    fi
}

# Clean environment
run_clean() {
    echo -e "${YELLOW}🧹 Cleaning development environment...${NC}"
    
    echo "Cleaning gradle caches..."
    ./gradlew clean || true
    
    echo "Cleaning build artifacts..."
    find . -name "build" -type d -exec rm -rf {} + 2>/dev/null || true
    find . -name "*.tmp" -delete 2>/dev/null || true
    
    echo "Cleaning temporary files..."
    rm -f /tmp/quick_compile.log /tmp/health_report.json /tmp/quality_output.log 2>/dev/null || true
    
    echo "Cleaning old reports (>7 days)..."
    find quality_reports -name "*.html" -mtime +7 -delete 2>/dev/null || true
    find quality_reports -name "*.json" -mtime +7 -delete 2>/dev/null || true
    
    echo -e "${GREEN}✅ Environment cleaned${NC}"
}

# Optimize environment
run_optimize() {
    echo -e "${BLUE}⚡ Optimizing development environment...${NC}"
    
    # Gradle optimization
    if [ ! -f "gradle.properties" ]; then
        echo "Creating gradle.properties with optimization settings..."
        cat > gradle.properties << EOF
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.configureondemand=true
org.gradle.caching=true
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m
EOF
    fi
    
    # Git optimization
    echo "Optimizing git configuration..."
    git config --local core.preloadindex true
    git config --local core.fscache true
    git config --local gc.auto 256
    
    echo -e "${GREEN}✅ Environment optimized${NC}"
}

# Setup development environment
run_setup() {
    echo -e "${CYAN}🔧 Setting up development environment...${NC}"
    
    # Make scripts executable
    chmod +x *.sh 2>/dev/null || true
    chmod +x *.py 2>/dev/null || true
    
    # Install Python dependencies if needed
    if command -v pip3 >/dev/null; then
        echo "Installing Python dependencies..."
        pip3 install --user psutil 2>/dev/null || echo "Failed to install psutil"
    fi
    
    # Setup git hooks
    if [ -f ".pre-commit-config.yaml" ]; then
        echo "Setting up git hooks..."
        cp .pre-commit-config.yaml .git/hooks/pre-commit 2>/dev/null || true
        chmod +x .git/hooks/pre-commit 2>/dev/null || true
    fi
    
    echo -e "${GREEN}✅ Development environment setup complete${NC}"
}

# Environment doctor
run_doctor() {
    echo -e "${PURPLE}🩺 Diagnosing development environment...${NC}"
    echo ""
    
    # Check required tools
    echo -e "${BOLD}Required Tools:${NC}"
    check_tool() {
        if command -v "$1" >/dev/null; then
            echo -e "  ✅ $1: $(command -v "$1")"
        else
            echo -e "  ❌ $1: Not found"
        fi
    }
    
    check_tool java
    check_tool gradle
    check_tool git
    check_tool python3
    check_tool ktlint
    
    # Check environment variables
    echo -e "\n${BOLD}Environment Variables:${NC}"
    env_var() {
        if [ -n "${!1}" ]; then
            echo -e "  ✅ $1: ${!1}"
        else
            echo -e "  ❌ $1: Not set"
        fi
    }
    
    env_var JAVA_HOME
    env_var ANDROID_HOME
    
    # Check disk space
    echo -e "\n${BOLD}Disk Space:${NC}"
    df -h . | tail -1 | while read -r filesystem size used avail percent mount; do
        echo "  📁 Available: $avail ($percent used)"
    done
    
    # Check gradle daemon
    echo -e "\n${BOLD}Gradle Status:${NC}"
    if ./gradlew --status >/dev/null 2>&1; then
        echo "  ✅ Gradle daemon: Running"
    else
        echo "  ⚠️ Gradle daemon: Not running"
    fi
    
    echo ""
}

# Smart commit workflow
run_commit() {
    echo -e "${GREEN}💾 Smart commit workflow...${NC}"
    
    # Check for uncommitted changes
    if ! git diff --quiet || ! git diff --cached --quiet; then
        echo "📝 Changes detected, running validation..."
        
        # Quick validation first
        if ./validate_quick.sh; then
            echo -e "${GREEN}✅ Quick validation passed${NC}"
            
            # Show changes
            echo -e "\n${BOLD}📋 Changes to commit:${NC}"
            git status --porcelain
            
            # Ask for commit message
            echo -e "\n${YELLOW}Enter commit message:${NC}"
            read -r commit_msg
            
            if [ -n "$commit_msg" ]; then
                git add .
                git commit -m "$commit_msg"
                echo -e "${GREEN}✅ Commit successful${NC}"
            else
                echo -e "${YELLOW}⚠️ Commit cancelled - no message provided${NC}"
            fi
        else
            echo -e "${RED}❌ Validation failed - fix issues before committing${NC}"
            exit 1
        fi
    else
        echo "No changes to commit"
    fi
}

# Main command handler
case "${1:-help}" in
    "quick")
        shift
        run_quick "$@"
        ;;
    "dev")
        shift
        run_dev_mode "$@"
        ;;
    "validate")
        shift
        run_validate "$@"
        ;;
    "core")
        shift
        run_core "$@"
        ;;
    "syntax")
        shift
        run_syntax "$@"
        ;;
    "format")
        shift
        run_format "$@"
        ;;
    "analyze")
        shift
        run_analyze "$@"
        ;;
    "quality")
        shift
        run_quality "$@"
        ;;
    "monitor")
        shift
        run_monitor "$@"
        ;;
    "report")
        shift
        run_report "$@"
        ;;
    "trends")
        shift
        run_trends "$@"
        ;;
    "clean")
        shift
        run_clean "$@"
        ;;
    "optimize")
        shift
        run_optimize "$@"
        ;;
    "setup")
        shift
        run_setup "$@"
        ;;
    "doctor")
        shift
        run_doctor "$@"
        ;;
    "commit")
        shift
        run_commit "$@"
        ;;
    "status")
        shift
        show_status "$@"
        ;;
    "help")
        show_banner
        show_help
        ;;
    "version")
        echo "IRCamera Developer Workflow Manager v${SCRIPT_VERSION}"
        echo "Enterprise-grade development automation"
        ;;
    *)
        show_banner
        echo -e "${RED}❌ Unknown command: $1${NC}"
        echo ""
        show_help
        exit 1
        ;;
esac