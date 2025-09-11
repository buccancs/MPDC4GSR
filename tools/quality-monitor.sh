#!/bin/bash

# IRCamera Quality Monitor
# Real-time project quality monitoring and metrics

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m'

# Configuration
MONITOR_INTERVAL=30
QUALITY_THRESHOLD=85
SECURITY_THRESHOLD=90

show_header() {
    clear
    echo -e "${BLUE}╔══════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║              🔍 IRCamera Quality Monitor                      ║${NC}"
    echo -e "${BLUE}║                Real-time Quality Metrics                     ║${NC}"
    echo -e "${BLUE}╚══════════════════════════════════════════════════════════════╝${NC}"
    echo ""
    echo -e "${CYAN}Last Updated: $(date '+%Y-%m-%d %H:%M:%S')${NC}"
    echo -e "${CYAN}Monitor Interval: ${MONITOR_INTERVAL}s${NC}"
    echo ""
}

calculate_project_metrics() {
    local kotlin_files java_files xml_files python_files
    local total_lines
    
    kotlin_files=$(find . -name "*.kt" -not -path "./build/*" | wc -l)
    java_files=$(find . -name "*.java" -not -path "./build/*" | wc -l)
    xml_files=$(find . -name "*.xml" -not -path "./build/*" | wc -l)
    python_files=$(find . -name "*.py" | wc -l)
    
    total_lines=$(find . \( -name "*.kt" -o -name "*.java" -o -name "*.py" \) -not -path "./build/*" -exec wc -l {} + 2>/dev/null | tail -1 | awk '{print $1}' || echo "0")
    
    # Calculate complexity score (simplified)
    local complexity_score=75
    if [ "$total_lines" -gt 50000 ]; then
        complexity_score=85
    elif [ "$total_lines" -gt 100000 ]; then
        complexity_score=90
    fi
    
    echo "kotlin_files=$kotlin_files"
    echo "java_files=$java_files"
    echo "xml_files=$xml_files"
    echo "python_files=$python_files"
    echo "total_lines=$total_lines"
    echo "complexity_score=$complexity_score"
}

check_build_health() {
    echo -e "${YELLOW}🔧 Build Health Check${NC}"
    echo "─────────────────────"
    
    if ./gradlew help --quiet &>/dev/null; then
        echo -e "Gradle Build: ${GREEN}✅ Healthy${NC}"
        local build_score=100
    else
        echo -e "Gradle Build: ${RED}❌ Issues Detected${NC}"
        local build_score=50
    fi
    
    if [ -x "./dev.sh" ]; then
        echo -e "Dev Tools: ${GREEN}✅ Ready${NC}"
    else
        echo -e "Dev Tools: ${YELLOW}⚠️  Not Executable${NC}"
        build_score=$((build_score - 10))
    fi
    
    echo "build_score=$build_score"
}

analyze_code_quality() {
    echo -e "${PURPLE}📊 Code Quality Analysis${NC}"
    echo "────────────────────────"
    
    local quality_score=0
    local checks_passed=0
    local total_checks=0
    
    # Kotlin quality check
    total_checks=$((total_checks + 1))
    if command -v ktlint &> /dev/null; then
        if find . -name "*.kt" -not -path "./build/*" -print0 | xargs -0 ktlint --reporter=plain &>/dev/null; then
            echo -e "Kotlin Style: ${GREEN}✅ Compliant${NC}"
            checks_passed=$((checks_passed + 1))
        else
            echo -e "Kotlin Style: ${YELLOW}⚠️  Issues Found${NC}"
        fi
    else
        echo -e "Kotlin Style: ${CYAN}➖ Not Available${NC}"
    fi
    
    # Python quality check
    total_checks=$((total_checks + 1))
    if command -v flake8 &> /dev/null; then
        if find . -name "*.py" -print0 | xargs -0 flake8 &>/dev/null; then
            echo -e "Python Style: ${GREEN}✅ Compliant${NC}"
            checks_passed=$((checks_passed + 1))
        else
            echo -e "Python Style: ${YELLOW}⚠️  Issues Found${NC}"
        fi
    else
        echo -e "Python Style: ${CYAN}➖ Not Available${NC}"
    fi
    
    # YAML quality check
    total_checks=$((total_checks + 1))
    if command -v yamllint &> /dev/null; then
        if find . \( -name "*.yml" -o -name "*.yaml" \) -print0 | xargs -0 yamllint -d relaxed &>/dev/null; then
            echo -e "YAML Format: ${GREEN}✅ Valid${NC}"
            checks_passed=$((checks_passed + 1))
        else
            echo -e "YAML Format: ${YELLOW}⚠️  Issues Found${NC}"
        fi
    else
        echo -e "YAML Format: ${CYAN}➖ Not Available${NC}"
    fi
    
    # Calculate quality score
    if [ "$total_checks" -gt 0 ]; then
        quality_score=$(( (checks_passed * 100) / total_checks ))
    fi
    
    echo "quality_score=$quality_score"
    echo "checks_passed=$checks_passed"
    echo "total_checks=$total_checks"
}

check_security_status() {
    echo -e "${RED}🛡️  Security Status${NC}"
    echo "──────────────────"
    
    local security_score=100
    local vulnerabilities=0
    
    # Check for common security issues
    if grep -r "TODO.*SECURITY\|FIXME.*SECURITY" . --include="*.kt" --include="*.java" &>/dev/null; then
        echo -e "Security TODOs: ${YELLOW}⚠️  Found${NC}"
        security_score=$((security_score - 10))
        vulnerabilities=$((vulnerabilities + 1))
    else
        echo -e "Security TODOs: ${GREEN}✅ None${NC}"
    fi
    
    if grep -r "password\|secret\|key" . --include="*.properties" --include="*.xml" | grep -v "android:key\|keyAlias" &>/dev/null; then
        echo -e "Exposed Secrets: ${RED}❌ Potential Issues${NC}"
        security_score=$((security_score - 20))
        vulnerabilities=$((vulnerabilities + 2))
    else
        echo -e "Exposed Secrets: ${GREEN}✅ Clean${NC}"
    fi
    
    echo "security_score=$security_score"
    echo "vulnerabilities=$vulnerabilities"
}

show_quality_dashboard() {
    local metrics
    metrics=$(calculate_project_metrics)
    
    eval "$metrics"
    
    echo -e "${GREEN}📈 Project Metrics${NC}"
    echo "─────────────────"
    echo -e "Kotlin Files: ${CYAN}$kotlin_files${NC}"
    echo -e "Java Files: ${CYAN}$java_files${NC}"
    echo -e "XML Resources: ${CYAN}$xml_files${NC}"
    echo -e "Python Scripts: ${CYAN}$python_files${NC}"
    echo -e "Total Lines: ${CYAN}$total_lines${NC}"
    echo ""
    
    # Build health
    local build_metrics
    build_metrics=$(check_build_health)
    eval "$build_metrics"
    echo ""
    
    # Quality analysis
    local quality_metrics
    quality_metrics=$(analyze_code_quality)
    eval "$quality_metrics"
    echo ""
    
    # Security status
    local security_metrics
    security_metrics=$(check_security_status)
    eval "$security_metrics"
    echo ""
    
    # Overall score
    local overall_score
    overall_score=$(( (build_score + quality_score + security_score) / 3 ))
    
    echo -e "${BLUE}🏆 Overall Quality Score${NC}"
    echo "───────────────────────"
    
    if [ "$overall_score" -ge 90 ]; then
        echo -e "Score: ${GREEN}$overall_score/100 ⭐⭐⭐${NC}"
    elif [ "$overall_score" -ge 75 ]; then
        echo -e "Score: ${YELLOW}$overall_score/100 ⭐⭐${NC}"
    else
        echo -e "Score: ${RED}$overall_score/100 ⭐${NC}"
    fi
    
    echo -e "Build Health: ${CYAN}$build_score/100${NC}"
    echo -e "Code Quality: ${CYAN}$quality_score/100${NC}"
    echo -e "Security: ${CYAN}$security_score/100${NC}"
    echo ""
    
    # Recommendations
    echo -e "${YELLOW}💡 Recommendations${NC}"
    echo "─────────────────"
    
    if [ "$build_score" -lt 100 ]; then
        echo -e "• ${YELLOW}Fix build issues for better stability${NC}"
    fi
    
    if [ "$quality_score" -lt "$QUALITY_THRESHOLD" ]; then
        echo -e "• ${YELLOW}Improve code formatting and style compliance${NC}"
    fi
    
    if [ "$security_score" -lt "$SECURITY_THRESHOLD" ]; then
        echo -e "• ${RED}Address security vulnerabilities${NC}"
    fi
    
    if [ "$overall_score" -ge 85 ]; then
        echo -e "• ${GREEN}Great work! Quality standards are being maintained${NC}"
    fi
}

monitor_continuous() {
    echo -e "${CYAN}Starting continuous monitoring (Press Ctrl+C to stop)...${NC}"
    echo ""
    
    while true; do
        show_header
        show_quality_dashboard
        
        echo ""
        echo -e "${CYAN}Next update in ${MONITOR_INTERVAL}s...${NC}"
        
        sleep "$MONITOR_INTERVAL"
    done
}

show_help() {
    echo -e "${BLUE}IRCamera Quality Monitor${NC}"
    echo ""
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  monitor   - Start continuous monitoring"
    echo "  snapshot  - Take single quality snapshot"
    echo "  help      - Show this help"
}

# Main command handler
case "${1:-monitor}" in
    "monitor")
        monitor_continuous
        ;;
    "snapshot")
        show_header
        show_quality_dashboard
        ;;
    "help"|*)
        show_help
        ;;
esac