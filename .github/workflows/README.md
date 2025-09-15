# GitHub Actions Workflows

This directory contains automated CI/CD workflows for the IRCamera project.

## 🔄 Core Workflows

### 1. **ci.yml** - Main CI Pipeline
**Triggers:** Push to main/develop, Pull Requests  
**Features:**
- Code formatting and linting
- Build validation
- Test execution
- Artifact upload on failure

### 2. **build-variants.yml** - Multi-Target Builds
**Purpose:** Build APK variants for different targets
- Google Play Release
- Topdon Release  
- Debug Build

### 3. **release.yml** - Release Management
**Triggers:** Tag creation (v*.*.*)
- Automated GitHub releases
- Signed release artifacts

### 4. **build-release.yml** - Release Builds
**Purpose:** Production build automation

### 5. **dependency-check.yml** - Security Scanning
**Features:**
- OWASP dependency vulnerability scanning
- License compliance checking

### 6. **codeql-analysis.yml** - CodeQL Security Analysis
**Triggers:** Push to main/develop/copilot branches, Pull Requests, Weekly schedule  
**Features:**
- Static code analysis for Java and Kotlin
- Security vulnerability detection
- Android-specific security checks
- SARIF report generation
- Integration with GitHub Security tab

## 🔧 Local Development

Use the local development script to mirror CI checks:
```bash
./dev.sh validate    # Run all checks
./dev.sh format      # Format code
./dev.sh lint        # Run linting
./dev.sh codeql      # Run CodeQL analysis (if CLI installed)
./dev.sh build       # Build project
```

- OWASP Dependency Check
- Vulnerability scanning
- Dependency update reports
- Security report artifacts

### 5. `build-variants.yml` - Build Variants

**Trigger:** Manual dispatch **Purpose:** Build specific variants (Google/Topdon)

**Features:**

- Multiple build variants
- Debug/Release builds
- Integration with existing build scripts
- Comprehensive build outputs

### 6. `code-formatting.yml` - Code Formatting & Validation

**Trigger:** Push to main branches, Pull Requests, Manual dispatch **Purpose:** Comprehensive code
formatting and validation across all file types

**Features:**

- XML file formatting (AndroidManifest, layouts, drawables, values)
- JSON file validation and formatting with proper indentation
- Gradle file syntax validation with dependency analysis
- YAML file linting with standards compliance
- TOML file validation and formatting
- Properties file standardization with key-value formatting
- Markdown file formatting for documentation consistency
- Shell script validation with executable permissions
- Chinese text cleanup from strings.xml files
- Automated commit functionality for formatting changes
- Comprehensive formatting reports with statistics

## Secrets Configuration

For full functionality, configure these secrets in your repository:

### Required for Release Signing:

- `SIGNING_KEY_ALIAS`: Keystore alias
- `SIGNING_KEY_PASSWORD`: Key password
- `SIGNING_STORE_PASSWORD`: Keystore password

### Optional for Enhanced Features:

- `GITHUB_TOKEN`: Automatically available for GitHub Actions

## Caching Strategy

All workflows use Gradle caching to improve build performance:

- Gradle packages cache
- Gradle wrapper cache
- Build cache for faster incremental builds

## Artifacts

Each workflow generates relevant artifacts:

- **APK files**: Debug and release builds
- **Test reports**: JUnit and lint results
- **Security reports**: Dependency and vulnerability scans
- **Build summaries**: Detailed build information

## Usage Examples

### Manual Release Build

1. Go to Actions tab
2. Select "Release Build"
3. Click "Run workflow"
4. Specify version and build type
5. Download artifacts from the workflow run

### Custom Variant Build

1. Go to Actions tab
2. Select "Build Variants"
3. Choose variant (google/topdon) and build type
4. Monitor build progress and download results

### Comprehensive Code Formatting

1. Go to Actions tab
2. Select "Code Formatting & Validation"
3. Click "Run workflow"
4. Enable "Auto-commit formatting changes" if desired
5. Review the formatting report in artifacts

**Local Formatting:** Run the local formatting script:

```bash
./format_all_files.sh
```

Or use pre-commit hooks:

```bash
pip install pre-commit
pre-commit install
pre-commit run --all-files
```

## Integration with Existing Scripts

The CI/CD pipeline integrates with existing build scripts:

- `build_production_apk.sh`
- `enhanced_build.sh`
- Individual variant build scripts

## Monitoring and Notifications

- Build status is visible on the repository main page
- PR checks prevent merging broken code
- Security scans run automatically
- Dependency updates are monitored weekly

## Troubleshooting

### Common Issues:

1. **Build Failures**: Check the logs for specific error messages
2. **Cache Issues**: Clear cache by re-running the workflow
3. **Signing Issues**: Verify secrets are correctly configured
4. **Test Failures**: Review test reports in artifacts

### Performance Optimization:

- Workflows use caching to reduce build times
- Matrix builds run in parallel
- Module-specific testing for PRs reduces unnecessary work

## Contributing

When adding new workflows:

1. Follow the existing naming convention
2. Include proper caching strategies
3. Generate relevant artifacts
4. Update this documentation
5. Test thoroughly before merging
