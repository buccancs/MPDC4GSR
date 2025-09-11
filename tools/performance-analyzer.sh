#!/bin/bash

# IRCamera Performance Analyzer
# Analyzes build performance, code complexity, and optimization opportunities

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m'

show_header() {
    echo -e "${BLUE}╔══════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║              ⚡ IRCamera Performance Analyzer                 ║${NC}"
    echo -e "${BLUE}║            Build Speed & Code Optimization                   ║${NC}"
    echo -e "${BLUE}╚══════════════════════════════════════════════════════════════╝${NC}"
    echo ""
}

measure_build_performance() {
    echo -e "${YELLOW}🏗️  Build Performance Analysis${NC}"
    echo "────────────────────────────────"
    
    # Clean build to get accurate timing
    echo "Cleaning previous build artifacts..."
    ./gradlew clean --quiet 2>/dev/null || true
    
    echo "Measuring build performance..."
    local start_time end_time duration
    
    start_time=$(date +%s)
    
    if ./gradlew :app:assembleDebug --quiet 2>/dev/null; then
        end_time=$(date +%s)
        duration=$((end_time - start_time))
        
        echo -e "Build Status: ${GREEN}✅ Success${NC}"
        echo -e "Build Time: ${CYAN}${duration}s${NC}"
        
        # Performance rating
        if [ "$duration" -lt 60 ]; then
            echo -e "Performance: ${GREEN}⚡ Excellent (<1min)${NC}"
        elif [ "$duration" -lt 120 ]; then
            echo -e "Performance: ${YELLOW}⚠️  Good (1-2min)${NC}"
        else
            echo -e "Performance: ${RED}❌ Slow (>2min)${NC}"
        fi
    else
        echo -e "Build Status: ${RED}❌ Failed${NC}"
        duration=0
    fi
    
    echo "build_duration=$duration"
}

analyze_code_complexity() {
    echo -e "${PURPLE}🔍 Code Complexity Analysis${NC}"
    echo "──────────────────────────────"
    
    local kotlin_files java_files total_lines avg_file_size
    local large_files complex_files
    
    kotlin_files=$(find . -name "*.kt" -not -path "./build/*" | wc -l)
    java_files=$(find . -name "*.java" -not -path "./build/*" | wc -l)
    
    # Calculate total lines and average file size
    if [ "$kotlin_files" -gt 0 ] || [ "$java_files" -gt 0 ]; then
        total_lines=$(find . \( -name "*.kt" -o -name "*.java" \) -not -path "./build/*" -exec wc -l {} + 2>/dev/null | tail -1 | awk '{print $1}' || echo "0")
        local total_files=$((kotlin_files + java_files))
        avg_file_size=$((total_lines / total_files))
    else
        total_lines=0
        avg_file_size=0
    fi
    
    # Find large files (>500 lines)
    large_files=$(find . \( -name "*.kt" -o -name "*.java" \) -not -path "./build/*" -exec wc -l {} + 2>/dev/null | awk '$1 > 500 {count++} END {print count+0}')
    
    # Find potentially complex files (>1000 lines)
    complex_files=$(find . \( -name "*.kt" -o -name "*.java" \) -not -path "./build/*" -exec wc -l {} + 2>/dev/null | awk '$1 > 1000 {count++} END {print count+0}')
    
    echo -e "Total Files: ${CYAN}$total_files${NC} ($kotlin_files Kotlin, $java_files Java)"
    echo -e "Total Lines: ${CYAN}$(printf "%'d" "$total_lines")${NC}"
    echo -e "Average File Size: ${CYAN}$avg_file_size lines${NC}"
    echo -e "Large Files (>500 lines): ${YELLOW}$large_files${NC}"
    echo -e "Complex Files (>1000 lines): ${RED}$complex_files${NC}"
    
    # Complexity rating
    local complexity_score=100
    if [ "$large_files" -gt 10 ]; then
        complexity_score=$((complexity_score - 20))
    fi
    if [ "$complex_files" -gt 5 ]; then
        complexity_score=$((complexity_score - 30))
    fi
    if [ "$avg_file_size" -gt 300 ]; then
        complexity_score=$((complexity_score - 10))
    fi
    
    echo -e "Complexity Score: ${CYAN}$complexity_score/100${NC}"
    
    echo "complexity_score=$complexity_score"
    echo "large_files=$large_files"
    echo "complex_files=$complex_files"
}

check_optimization_opportunities() {
    echo -e "${GREEN}🚀 Optimization Opportunities${NC}"
    echo "───────────────────────────────"
    
    local optimizations=0
    
    # Check for unused imports (simplified)
    local unused_imports
    unused_imports=$(find . -name "*.kt" -not -path "./build/*" -exec grep -l "^import.*\.\*" {} \; 2>/dev/null | wc -l)
    if [ "$unused_imports" -gt 0 ]; then
        echo -e "• ${YELLOW}Wildcard imports found: $unused_imports files${NC}"
        optimizations=$((optimizations + 1))
    fi
    
    # Check for TODO/FIXME comments
    local todo_count
    todo_count=$(find . \( -name "*.kt" -o -name "*.java" \) -not -path "./build/*" -exec grep -l "TODO\|FIXME" {} \; 2>/dev/null | wc -l)
    if [ "$todo_count" -gt 0 ]; then
        echo -e "• ${YELLOW}TODO/FIXME items: $todo_count files${NC}"
        optimizations=$((optimizations + 1))
    fi
    
    # Check for old Android API usage
    local old_api_usage
    old_api_usage=$(find . -name "*.kt" -o -name "*.java" -not -path "./build/*" -exec grep -l "getDefaultSharedPreferences\|AsyncTask" {} \; 2>/dev/null | wc -l)
    if [ "$old_api_usage" -gt 0 ]; then
        echo -e "• ${RED}Deprecated API usage: $old_api_usage files${NC}"
        optimizations=$((optimizations + 1))
    fi
    
    # Check for hardcoded strings
    local hardcoded_strings
    hardcoded_strings=$(find . -name "*.kt" -o -name "*.java" -not -path "./build/*" -exec grep -l '"[A-Za-z].*[A-Za-z]"' {} \; 2>/dev/null | wc -l)
    if [ "$hardcoded_strings" -gt 10 ]; then
        echo -e "• ${YELLOW}Potential hardcoded strings: $hardcoded_strings files${NC}"
        optimizations=$((optimizations + 1))
    fi
    
    if [ "$optimizations" -eq 0 ]; then
        echo -e "• ${GREEN}No major optimization issues found${NC}"
    fi
    
    echo "optimization_opportunities=$optimizations"
}

analyze_dependencies() {
    echo -e "${CYAN}📦 Dependency Analysis${NC}"
    echo "─────────────────────"
    
    local gradle_files dependency_count
    gradle_files=$(find . -name "*.gradle*" | wc -l)
    
    # Count dependencies in gradle files
    dependency_count=$(find . -name "*.gradle*" -exec grep -h "implementation\|api\|compile" {} \; 2>/dev/null | wc -l)
    
    echo -e "Gradle Files: ${CYAN}$gradle_files${NC}"
    echo -e "Dependencies: ${CYAN}$dependency_count${NC}"
    
    # Check for version conflicts (simplified)
    local version_conflicts
    version_conflicts=$(find . -name "*.gradle*" -exec grep -h "force\|exclude" {} \; 2>/dev/null | wc -l)
    if [ "$version_conflicts" -gt 0 ]; then
        echo -e "Potential Version Conflicts: ${YELLOW}$version_conflicts${NC}"
    else
        echo -e "Version Conflicts: ${GREEN}None detected${NC}"
    fi
    
    echo "dependency_count=$dependency_count"
    echo "version_conflicts=$version_conflicts"
}

generate_performance_report() {
    echo -e "${BLUE}📊 Performance Summary Report${NC}"
    echo "═══════════════════════════════"
    
    # Collect all metrics
    local build_metrics complexity_metrics optimization_metrics dependency_metrics
    local build_duration complexity_score optimization_opportunities dependency_count
    
    build_metrics=$(measure_build_performance)
    eval "$build_metrics"
    echo ""
    
    complexity_metrics=$(analyze_code_complexity)
    eval "$complexity_metrics"
    echo ""
    
    optimization_metrics=$(check_optimization_opportunities)
    eval "$optimization_metrics"
    echo ""
    
    dependency_metrics=$(analyze_dependencies)
    eval "$dependency_metrics"
    echo ""
    
    # Calculate overall performance score
    local performance_score=100
    
    # Build performance impact
    if [ "$build_duration" -gt 120 ]; then
        performance_score=$((performance_score - 20))
    elif [ "$build_duration" -gt 60 ]; then
        performance_score=$((performance_score - 10))
    fi
    
    # Complexity impact
    performance_score=$((performance_score - (100 - complexity_score) / 4))
    
    # Optimization opportunities impact
    performance_score=$((performance_score - optimization_opportunities * 5))
    
    # Ensure minimum score
    if [ "$performance_score" -lt 0 ]; then
        performance_score=0
    fi
    
    echo -e "${BLUE}🏆 Overall Performance Score${NC}"
    echo "──────────────────────────────"
    
    if [ "$performance_score" -ge 90 ]; then
        echo -e "Score: ${GREEN}$performance_score/100 🚀 Excellent${NC}"
    elif [ "$performance_score" -ge 75 ]; then
        echo -e "Score: ${YELLOW}$performance_score/100 ⚡ Good${NC}"
    else
        echo -e "Score: ${RED}$performance_score/100 🐌 Needs Improvement${NC}"
    fi
    
    echo ""
    echo -e "${YELLOW}📋 Key Metrics${NC}"
    echo "─────────────"
    echo -e "Build Time: ${CYAN}${build_duration}s${NC}"
    echo -e "Code Complexity: ${CYAN}$complexity_score/100${NC}"
    echo -e "Large Files: ${CYAN}$large_files${NC}"
    echo -e "Optimization Issues: ${CYAN}$optimization_opportunities${NC}"
    echo -e "Dependencies: ${CYAN}$dependency_count${NC}"
    
    echo ""
    echo -e "${GREEN}🎯 Recommendations${NC}"
    echo "─────────────────"
    
    if [ "$build_duration" -gt 60 ]; then
        echo -e "• ${YELLOW}Consider enabling Gradle build cache and parallel builds${NC}"
    fi
    
    if [ "$large_files" -gt 5 ]; then
        echo -e "• ${YELLOW}Refactor large files into smaller, focused classes${NC}"
    fi
    
    if [ "$optimization_opportunities" -gt 0 ]; then
        echo -e "• ${YELLOW}Address optimization opportunities to improve maintainability${NC}"
    fi
    
    if [ "$performance_score" -ge 85 ]; then
        echo -e "• ${GREEN}Performance is well optimized! Keep up the good work${NC}"
    fi
}

show_help() {
    echo -e "${BLUE}IRCamera Performance Analyzer${NC}"
    echo ""
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  build      - Analyze build performance only"
    echo "  complexity - Analyze code complexity only"
    echo "  optimize   - Check optimization opportunities"
    echo "  deps       - Analyze dependencies"
    echo "  full       - Complete performance analysis (default)"
    echo "  help       - Show this help"
}

# Main command handler
case "${1:-full}" in
    "build")
        show_header
        measure_build_performance
        ;;
    "complexity")
        show_header
        analyze_code_complexity
        ;;
    "optimize")
        show_header
        check_optimization_opportunities
        ;;
    "deps")
        show_header
        analyze_dependencies
        ;;
    "full")
        show_header
        generate_performance_report
        ;;
    "help"|*)
        show_help
        ;;
esac