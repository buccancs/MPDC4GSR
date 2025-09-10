#!/bin/bash

# Consolidated Developer Workflow - Unified interface for all development operations
# Usage: ./dev_workflow_simplified.sh [command]

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}🚀 IRCamera Dev Workflow v4.0${NC}"
    echo -e "${BLUE}================================${NC}"
}

show_menu() {
    echo -e "${YELLOW}Available commands:${NC}"
    echo "  quick     - Quick validation (15s)"
    echo "  validate  - Core validation (60s)"
    echo "  full      - Full validation + tests (300s)"
    echo "  format    - Format code files"
    echo "  analyze   - Quality analysis"
    echo "  build     - Build project"
    echo "  clean     - Clean build artifacts"
    echo "  commit    - Smart commit with validation"
    echo "  help      - Show this help"
}

run_command() {
    local cmd="$1"

    case $cmd in
        "quick")
            echo -e "${GREEN}Running quick validation...${NC}"
            ./validate.sh quick
            ;;
        "validate")
            echo -e "${GREEN}Running core validation...${NC}"
            ./validate.sh core
            ;;
        "full")
            echo -e "${GREEN}Running full validation...${NC}"
            ./validate.sh full
            ;;
        "format")
            echo -e "${GREEN}Formatting code...${NC}"
            ./quality_format.sh format
            ;;
        "analyze")
            echo -e "${GREEN}Analyzing quality...${NC}"
            ./quality_format.sh analyze
            ;;
        "build")
            echo -e "${GREEN}Building project...${NC}"
            ./gradlew assembleDebug
            ;;
        "clean")
            echo -e "${GREEN}Cleaning build...${NC}"
            ./gradlew clean
            ;;
        "commit")
            echo -e "${GREEN}Smart commit workflow...${NC}"
            ./validate.sh core --auto-fix
            ./quality_format.sh full --commit
            ;;
        "help"|"")
            show_menu
            ;;
        *)
            echo -e "${RED}Unknown command: $cmd${NC}"
            show_menu
            exit 1
            ;;
    esac
}

main() {
    print_header

    if [[ $# -eq 0 ]]; then
        show_menu
        exit 0
    fi

    run_command "$1"

    echo -e "${GREEN}✅ Command completed successfully${NC}"
}

main "$@"