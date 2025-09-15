# CodeQL Security Analysis for IRCamera

## Overview

This directory contains the CodeQL configuration and documentation for the IRCamera Android application security analysis. CodeQL is GitHub's semantic code analysis engine that helps identify security vulnerabilities and code quality issues.

## What is CodeQL?

CodeQL treats code as data, allowing you to write queries to find patterns in your codebase that might indicate security vulnerabilities, bugs, or other issues. It's particularly effective for:

- **Security Vulnerabilities**: SQL injection, XSS, path traversal, etc.
- **Android-Specific Issues**: Intent injection, unsafe broadcasts, data leakage
- **Code Quality**: Null pointer dereferences, resource leaks, unused code
- **Performance Issues**: Inefficient algorithms, memory leaks

## Configuration Files

### `codeql-config.yml`
The main configuration file that defines:
- **Query Suites**: security-extended, security-and-quality
- **Path Filters**: Focus on source code, exclude build artifacts
- **Language Support**: Java and Kotlin analysis
- **Custom Settings**: Android-specific query filters

## Integration with CI/CD

### GitHub Actions Workflow
The CodeQL analysis runs automatically on:
- **Push to main/develop**: Every code change is analyzed
- **Pull Requests**: Security review before merging
- **Scheduled Scans**: Weekly deep analysis (Tuesdays at 2:30 AM UTC)

### Development Workflow
```bash
# Run CodeQL analysis locally (if CLI installed)
./dev.sh codeql

# View status of latest analysis
gh api repos/{owner}/{repo}/code-scanning/alerts
```

## Understanding Results

### Severity Levels
- **Error**: Critical security vulnerabilities requiring immediate attention
- **Warning**: Important issues that should be addressed
- **Note**: Code quality improvements and best practice recommendations

### Common Issue Categories

#### Security Issues
- **CWE-79**: Cross-site Scripting (XSS)
- **CWE-89**: SQL Injection
- **CWE-22**: Path Traversal
- **CWE-327**: Weak Cryptography
- **CWE-200**: Information Exposure

#### Android-Specific Issues
- **Intent Injection**: Unsafe intent handling
- **Broadcast Security**: Unprotected broadcast receivers
- **Content Provider**: Unsafe content provider access
- **Logging**: Sensitive data in logs
- **Network Security**: HTTP usage, certificate validation

#### Code Quality Issues
- **Null Pointer**: Potential null dereferences
- **Resource Leaks**: Unclosed files, streams, cursors
- **Dead Code**: Unused variables, methods, imports
- **Performance**: Inefficient string concatenation, boxing

## Viewing Results

### GitHub Security Tab
1. Navigate to your repository on GitHub
2. Click "Security" tab
3. Select "Code scanning alerts"
4. Filter by severity, category, or file

### SARIF Files
CodeQL generates SARIF (Static Analysis Results Interchange Format) files:
```bash
# View SARIF results (if jq installed)
jq '.runs[0].results[] | {rule: .ruleId, message: .message.text, file: .locations[0].physicalLocation.artifactLocation.uri}' results.sarif
```

## Local Development Setup

### Install CodeQL CLI (Optional)
```bash
# Download from GitHub
wget https://github.com/github/codeql-cli-binaries/releases/latest/download/codeql-linux64.zip
unzip codeql-linux64.zip
export PATH=$PATH:$(pwd)/codeql

# Verify installation
codeql version
```

### Running Analysis Locally
```bash
# Create database
codeql database create \
    --language=java,kotlin \
    --source-root=. \
    --command="./gradlew build -x test" \
    ./codeql-database

# Run analysis
codeql database analyze \
    ./codeql-database \
    --format=sarif-latest \
    --output=results.sarif \
    --download

# View results
codeql bqrs decode --format=csv ./codeql-database/results/*.bqrs
```

## Customizing Analysis

### Adding Custom Queries
Create `.ql` files in the `queries/` directory:
```ql
/**
 * @name Android Intent Injection
 * @description Detects potential intent injection vulnerabilities
 * @kind path-problem
 * @id android/intent-injection
 */

import java
import semmle.code.java.dataflow.TaintTracking

// Custom query implementation
```

### Modifying Configuration
Edit `codeql-config.yml` to:
- Include/exclude specific paths
- Add custom query suites
- Configure severity thresholds
- Set up custom filters

### Query Filters Example
```yaml
query-filters:
  - exclude:
      id: java/unused-local-variable
  - include:
      tags: security
      severity: [error, warning]
```

## Best Practices

### For Developers
1. **Review Alerts Promptly**: Address security issues before merging
2. **Understand Context**: Not all alerts are real vulnerabilities
3. **Test Fixes**: Ensure fixes don't break functionality
4. **Learn Patterns**: Understand common vulnerability patterns

### for Security Teams
1. **Regular Reviews**: Monitor security dashboard weekly
2. **Trend Analysis**: Track vulnerability trends over time
3. **Training**: Educate developers on secure coding
4. **Custom Rules**: Create organization-specific queries

### For DevOps
1. **Branch Protection**: Require CodeQL checks to pass
2. **Notification Setup**: Configure alerts for critical issues
3. **Integration**: Connect with issue tracking systems
4. **Metrics**: Track security improvement over time

## Troubleshooting

### Common Issues

#### Build Failures
```bash
# Ensure clean build
./gradlew clean build

# Check for compilation errors
./gradlew compileDebugJavaWithJavac --info
```

#### Database Creation Issues
```bash
# Verbose logging
codeql database create --verbose --debug ./db

# Check source files
find . -name "*.java" -o -name "*.kt" | head -10
```

#### Analysis Timeout
```bash
# Reduce analysis scope
codeql database analyze --max-paths=0 ./db

# Use specific query suites
codeql database analyze ./db codeql/java-queries:codeql-suites/java-security-extended.qls
```

### Performance Optimization
- **Incremental Analysis**: Use `--incremental` flag for faster rebuilds
- **Parallel Execution**: Use `--threads` option for multi-core systems
- **Memory Settings**: Adjust heap size with `-J-Xmx4G`

## Resources

### Documentation
- [CodeQL Documentation](https://codeql.github.com/docs/)
- [Query Reference](https://codeql.github.com/codeql-standard-libraries/)
- [Android Security Guide](https://developer.android.com/topic/security)

### Training Materials
- [CodeQL U-Boot](https://github.com/githubtraining/codeql-uboot)
- [Security Lab Challenges](https://securitylab.github.com/ctf)
- [Query Writing Tutorial](https://codeql.github.com/docs/writing-codeql-queries/)

### Community
- [CodeQL Discussions](https://github.com/github/codeql/discussions)
- [Security Research](https://securitylab.github.com/)
- [Android Security Blog](https://android-developers.googleblog.com/search/label/Security)

## Support

For issues with CodeQL analysis:
1. Check workflow logs in GitHub Actions
2. Review configuration files for syntax errors
3. Ensure build process works independently
4. Open issue with detailed error messages and environment information

---

**Note**: CodeQL analysis helps identify potential issues but requires human review to determine if findings represent actual security vulnerabilities in your specific context.