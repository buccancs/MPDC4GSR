# IRCamera - Multi-Device Thermal Imaging Platform

[![Android Build](https://img.shields.io/badge/Android-Kotlin-green.svg)](https://developer.android.com/)
[![PC Controller](https://img.shields.io/badge/PC%20Controller-Python-blue.svg)](https://www.python.org/)
[![Thermal Devices](https://img.shields.io/badge/Thermal-TC001%20%7C%20TS004%20%7C%20HIK-orange.svg)](https://www.topdon.com/)

A comprehensive thermal imaging application platform supporting multiple thermal camera devices with advanced imaging capabilities, data recording, and cross-platform synchronization.

## 🎯 Overview

IRCamera is a modular thermal imaging platform that consists of:
- **Android Application**: Feature-rich mobile thermal imaging with multi-device support
- **PC Controller**: Python-based hub for advanced data processing and device coordination
- **Multi-Device Support**: TC001, TC007, TS004, HIKVision thermal cameras
- **GSR Integration**: Shimmer3 sensor support for physiological data collection

### Key Features

- **Multi-Device Thermal Imaging**: Support for various thermal camera models
- **Real-Time Processing**: Live thermal image processing and analysis
- **Data Synchronization**: Cross-platform data collection and synchronization
- **Advanced Analysis**: 3D thermal reconstruction, temperature monitoring, and reporting
- **Modular Architecture**: Component-based design for easy feature extension

## 🏗️ System Architecture

The IRCamera platform uses a modular, component-based architecture designed for flexibility and scalability:

```mermaid
graph TB
    subgraph "Android Application Layer"
        MainApp[Main Application]
        UI[User Interface Layer]
        Services[Background Services]
    end
    
    subgraph "Feature Components"
        ThermalIR[Thermal-IR Module]
        ThermalLite[Thermal-Lite Module]
        GSRRec[GSR Recording Module]
        House[House Analysis Module]
        Edit3D[3D Edit Module]
        Transfer[Transfer Module]
        User[User Management Module]
        Pseudo[Pseudo Color Module]
        Common[Common Components]
    end
    
    subgraph "Core Libraries"
        LibApp[App Core Library]
        LibCom[Communication Library] 
        LibIR[IR Processing Library]
        LibUI[UI Components Library]
        LibHIK[HIKVision Integration]
        LibMatrix[Matrix Processing]
        LibMenu[Menu Components]
    end
    
    subgraph "Hardware Integration"
        BLE[BLE Module]
        RangeSeek[Range Seek Bar]
        Cameras[Camera Interfaces]
    end
    
    subgraph "PC Controller"
        PCCore[PC Core Engine]
        GSRIngest[GSR Data Ingestor]
        NetSync[Network Sync]
        DataProc[Data Processing]
    end
    
    MainApp --> UI
    MainApp --> Services
    UI --> ThermalIR
    UI --> ThermalLite
    UI --> GSRRec
    UI --> House
    UI --> Edit3D
    UI --> Transfer
    UI --> User
    UI --> Pseudo
    
    ThermalIR --> LibIR
    ThermalLite --> LibIR
    GSRRec --> LibCom
    House --> LibApp
    Edit3D --> LibMatrix
    Transfer --> LibCom
    User --> LibApp
    Pseudo --> LibIR
    Common --> LibUI
    
    Services --> BLE
    UI --> RangeSeek
    LibCom --> Cameras
    
    LibCom <-->|Network Protocol| PCCore
    GSRIngest --> NetSync
    NetSync --> DataProc
```

## 📱 Component Architecture

### Android App Module Structure

```mermaid
graph LR
    subgraph "Core Application"
        App[Main App Module]
        Common[Common Library]
    end
    
    subgraph "Thermal Processing Components"
        TIR[thermal-ir]
        TLite[thermal-lite]
        Thermal[thermal]
        Pseudo[pseudo]
    end
    
    subgraph "Data & Analysis Components"
        GSR[gsr-recording]
        House[house]
        Edit3D[edit3d]
        Transfer[transfer]
    end
    
    subgraph "User Interface Components"
        User[user]
        CommonComp[CommonComponent]
        RangeSeek[RangeSeekBar]
    end
    
    subgraph "Core Libraries"
        LibApp[libapp]
        LibCom[libcom]
        LibIR[libir]
        LibUI[libui]
        LibHIK[libhik]
        LibMatrix[libmatrix]
        LibMenu[libmenu]
    end
    
    subgraph "Hardware Integration"
        BLE[BleModule]
    end
    
    App --> TIR
    App --> TLite
    App --> Thermal
    App --> GSR
    App --> House
    App --> Edit3D
    App --> Transfer
    App --> User
    
    TIR --> LibIR
    TLite --> LibIR
    Thermal --> LibIR
    Pseudo --> LibIR
    
    GSR --> LibCom
    Transfer --> LibCom
    House --> LibApp
    User --> LibApp
    
    Edit3D --> LibMatrix
    CommonComp --> LibUI
    User --> LibMenu
    
    LibCom --> BLE
    Common --> LibApp
```

### PC Controller Architecture

```mermaid
graph TB
    subgraph "PC Controller Application"
        Main[main.py]
        Core[Core Engine]
        GUI[GUI Layer]
        Network[Network Layer]
        Utils[Utilities]
        Tests[Test Suite]
    end
    
    subgraph "Core Components"
        GSRIngest[GSR Data Ingestor]
        SessionMgr[Session Manager]
        DataAgg[Data Aggregator]
        TimSync[Time Synchronization]
    end
    
    subgraph "External Integrations"
        AndroidApp[Android App]
        ThermalCam[Thermal Cameras]
        ShimmerGSR[Shimmer GSR Sensors]
        Storage[Data Storage]
    end
    
    Main --> Core
    Main --> GUI
    Core --> GSRIngest
    Core --> SessionMgr
    Core --> DataAgg
    Core --> TimSync
    
    Network <-->|TCP/IP Protocol| AndroidApp
    GSRIngest <-->|BLE/Serial| ShimmerGSR
    GUI -->|Control Commands| Core
    DataAgg --> Storage
    
    SessionMgr -->|Coordinate| ThermalCam
    TimSync -->|Sync Protocol| AndroidApp
```

## 🔧 Feature Breakdown by Module

### Thermal Processing Modules

| Module | Purpose | Key Features |
|--------|---------|--------------|
| **thermal-ir** | Main thermal imaging | Real-time processing, temperature analysis, monitoring |
| **thermal-lite** | Lightweight thermal | Optimized for lower-end devices, basic thermal functions |
| **thermal** | Core thermal engine | Base thermal processing algorithms and utilities |
| **pseudo** | Pseudo coloring | False color mapping, thermal visualization enhancement |

### Data Collection & Analysis

| Module | Purpose | Key Features |
|--------|---------|--------------|
| **gsr-recording** | GSR data capture | Shimmer3 integration, physiological data recording |
| **house** | Building analysis | Thermal analysis for building inspection, energy auditing |
| **edit3d** | 3D reconstruction | 3D thermal model generation and editing |
| **transfer** | Data management | File transfer, synchronization, data export |

### User Interface & Controls

| Module | Purpose | Key Features |
|--------|---------|--------------|
| **user** | User management | Settings, preferences, user profiles |
| **CommonComponent** | Shared UI elements | Reusable components, common widgets |
| **RangeSeekBar** | Custom controls | Range selection, threshold setting |

### Core Libraries

| Library | Purpose | Key Features |
|---------|---------|--------------|
| **libapp** | Application core | Core app functionality, base classes |
| **libcom** | Communication | Network protocols, device communication |
| **libir** | IR processing | Thermal image processing algorithms |
| **libui** | UI framework | UI components and styling |
| **libhik** | HIKVision support | HIKVision camera integration |
| **libmatrix** | Matrix operations | Mathematical operations for image processing |
| **libmenu** | Menu system | Application menu and navigation |

## 🔄 Data Flow Architecture

```mermaid
flowchart TD
    subgraph "Hardware Layer"
        TC001[TC001 Camera]
        TC007[TC007 Camera] 
        TS004[TS004 Camera]
        HIK[HIKVision Camera]
        Shimmer[Shimmer3 GSR]
        RGB[RGB Camera]
    end
    
    subgraph "Android Sensor Processing"
        BLEMod[BLE Module]
        ThermalProc[Thermal Processing]
        GSRProc[GSR Processing]
        ImageProc[Image Processing]
        DataSync[Data Synchronization]
    end
    
    subgraph "PC Controller Hub"
        NetRx[Network Receiver]
        GSRIngest[GSR Ingestor]
        DataAgg[Data Aggregator]
        SessionCtrl[Session Controller]
        Storage[Data Storage]
    end
    
    subgraph "Output & Analysis"
        ThermalVideo[Thermal Video]
        GSRData[GSR Data CSV]
        RawImages[Raw Images]
        Analysis[Analysis Reports]
        Export[Data Export]
    end
    
    TC001 --> ThermalProc
    TC007 --> ThermalProc
    TS004 --> ThermalProc
    HIK --> ThermalProc
    Shimmer --> BLEMod
    RGB --> ImageProc
    
    BLEMod --> GSRProc
    ThermalProc --> DataSync
    GSRProc --> DataSync
    ImageProc --> DataSync
    
    DataSync -->|Network Protocol| NetRx
    NetRx --> DataAgg
    NetRx --> GSRIngest
    DataAgg --> SessionCtrl
    GSRIngest --> SessionCtrl
    SessionCtrl --> Storage
    
    Storage --> ThermalVideo
    Storage --> GSRData
    Storage --> RawImages
    Storage --> Analysis
    Storage --> Export
```

## 🚀 Quick Start

### Prerequisites
- Android Studio 4.0+ with Kotlin support
- Python 3.8+ for PC Controller
- Supported thermal camera device
- Android device with API 21+

### Building the Android Application

```bash
# Clone the repository
git clone https://github.com/buccancs/IRCamera.git
cd IRCamera

# Build all modules
./gradlew clean build

# Build specific release APK
./gradlew :app:assembleRelease

# Install on connected device
adb install app/build/outputs/apk/release/app-release.apk
```

### Setting up PC Controller

```bash
# Navigate to PC controller directory
cd pc-controller

# Install Python dependencies
pip install -r requirements.txt

# Run the application
python src/main.py
```

### Basic Usage Flow

1. **Device Connection**: Connect thermal camera via USB or network
2. **App Launch**: Start Android application and select device type
3. **PC Sync** (Optional): Launch PC controller for advanced features
4. **Recording**: Begin thermal imaging session
5. **Data Export**: Export collected data for analysis

## 📱 Supported Devices & Features

### Thermal Camera Support

| Device | Module | Features | Notes |
|--------|---------|----------|-------|
| **TC001** | thermal-ir | Full thermal imaging, temperature analysis | Primary thermal device |
| **TC001 Plus** | thermal-ir | Enhanced processing, higher resolution | Advanced features |
| **TC001 Lite** | thermal-lite | Basic thermal imaging, optimized performance | Entry-level device |
| **TC007** | thermal-ir | Wireless thermal imaging, battery operation | Portable thermal camera |
| **TS004** | thermal | Network-connected thermal device | IP-based thermal imaging |
| **HIKVision** | libhik | Enterprise thermal cameras | Professional-grade devices |

### Android App Features by Module

```mermaid
mindmap
  root((IRCamera Features))
    Thermal Imaging
      Real-time Processing
      Temperature Measurement
      Pseudo Color Mapping
      Video Recording
      Image Capture
    Data Collection
      GSR Recording
      Shimmer3 Integration
      Data Synchronization
      Session Management
    Analysis Tools
      3D Reconstruction
      Building Analysis
      Temperature Monitoring
      Report Generation
    User Interface
      Multi-device Support
      Settings Management
      Gallery View
      Data Transfer
    Hardware Integration
      BLE Connectivity
      USB Camera Support
      Network Protocols
      Device Discovery
```

## 🔧 Development Setup

### Project Structure Overview

```
IRCamera/
├── app/                    # Main Android application
├── pc-controller/          # Python PC application
├── component/              # Feature modules
│   ├── thermal-ir/         # Main thermal processing
│   ├── thermal-lite/       # Lightweight thermal
│   ├── gsr-recording/      # GSR data collection
│   ├── house/              # Building analysis
│   ├── edit3d/             # 3D editing tools
│   ├── transfer/           # Data transfer
│   ├── user/               # User management
│   ├── pseudo/             # Pseudo coloring
│   └── CommonComponent/    # Shared components
├── lib*/                   # Core libraries
│   ├── libapp/             # App framework
│   ├── libcom/             # Communication
│   ├── libir/              # IR processing
│   ├── libui/              # UI components
│   ├── libhik/             # HIKVision integration
│   ├── libmatrix/          # Matrix operations
│   └── libmenu/            # Menu system
├── BleModule/              # Bluetooth integration
└── RangeSeekBar/           # Custom UI control
```

### Key Technologies

- **Android Development**: Kotlin, MVVM Architecture, CameraX, Android Architecture Components
- **PC Controller**: Python 3.8+, GUI frameworks, data processing libraries
- **Communication**: Network protocols, BLE integration, device synchronization
- **Image Processing**: Thermal image algorithms, pseudo coloring, matrix operations
- **Hardware Integration**: Multiple thermal camera APIs, GSR sensor integration

### Adding New Components

1. **Create Module**: Add new module in `component/` directory
2. **Update Settings**: Add module to `settings.gradle.kts`
3. **Define Dependencies**: Configure `build.gradle.kts` for the module
4. **Implement Interface**: Follow existing patterns in similar modules
5. **Integration**: Wire module into main application

### Development Workflow

```bash
# 1. Setup development environment
./gradlew build

# 2. Run tests
./gradlew test

# 3. Build specific module
./gradlew :component:thermal-ir:build

# 4. Generate documentation
./gradlew dokka

# 5. Create release build
./gradlew assembleRelease
```

## 📊 Data Output Formats

### Thermal Data
```
thermal_session_YYYYMMDD_HHMMSS/
├── thermal_video.mp4       # Processed thermal video
├── raw_thermal/            # Raw thermal data frames
├── temperature_map.csv     # Temperature measurements
└── metadata.json          # Session configuration
```

### GSR Data (when using PC Controller)
```
gsr_session_YYYYMMDD_HHMMSS/
├── gsr_data.csv           # Time-series GSR measurements  
├── events.csv             # Synchronization events
├── raw_images/            # Synchronized image captures
└── session_info.json     # Recording metadata
```

## 🔄 Advanced System Diagrams

### Communication Sequence Diagram

```mermaid
sequenceDiagram
    participant A as Android App
    participant PC as PC Controller
    participant TC as Thermal Camera
    participant S as Shimmer GSR
    participant D as Data Storage
    
    A->>PC: Initial Connection Request
    PC->>A: Authentication Challenge
    A->>PC: Authentication Response
    PC->>A: Connection Established
    
    A->>TC: Initialize Camera
    TC->>A: Camera Ready
    A->>S: Connect BLE GSR
    S->>A: BLE Connected
    
    PC->>A: Start Recording Session
    A->>TC: Begin Thermal Capture
    A->>S: Begin GSR Recording
    
    loop Recording Session
        TC->>A: Thermal Frame Data
        S->>A: GSR Data Point
        A->>PC: Synchronized Data Packet
        PC->>D: Store Data with Timestamp
    end
    
    PC->>A: Stop Recording Session
    A->>TC: Stop Thermal Capture
    A->>S: Stop GSR Recording
    A->>PC: Session Complete
    PC->>D: Finalize Data Export
```

### Component Lifecycle State Diagram

```mermaid
stateDiagram-v2
    [*] --> Initializing
    Initializing --> Idle: Setup Complete
    Idle --> Connecting: User Connects Device
    Connecting --> Connected: Device Ready
    Connecting --> Error: Connection Failed
    Connected --> Recording: Start Session
    Recording --> Paused: User Pause
    Paused --> Recording: Resume
    Recording --> Processing: Stop Session
    Processing --> Idle: Processing Complete
    Error --> Idle: Reset/Retry
    Connected --> Idle: Disconnect
    Idle --> [*]: App Close
```

### Deployment Architecture

```mermaid
deployment
    node "Android Device" {
        component "IRCamera App" {
            [thermal-ir]
            [gsr-recording]
            [libir]
            [libcom]
        }
        database "Local Storage"
    }
    
    node "PC Controller" {
        component "Python Hub" {
            [Session Manager]
            [Data Aggregator]
            [GSR Ingestor]
        }
        database "Centralized Storage"
    }
    
    node "Thermal Hardware" {
        [TC001 Camera]
        [TC007 Camera]
        [TS004 Camera]
        [HIKVision Camera]
    }
    
    node "BLE Sensors" {
        [Shimmer3 GSR]
        [Custom Sensors]
    }
    
    [IRCamera App] --> [Python Hub]: TCP/IP Protocol
    [IRCamera App] --> [TC001 Camera]: USB/Wireless
    [IRCamera App] --> [Shimmer3 GSR]: BLE
    [Python Hub] --> [Centralized Storage]: File I/O
```

### Class Relationship Diagram

```mermaid
classDiagram
    class ThermalProcessor {
        +processFrame(frame: ThermalFrame)
        +applyPseudoColor(frame: ThermalFrame)
        +extractTemperatureData(frame: ThermalFrame)
        +calibrateDevice(device: ThermalDevice)
    }
    
    class GSRRecorder {
        +connectDevice(address: String)
        +startRecording()
        +stopRecording()
        +processGSRData(data: ByteArray)
    }
    
    class DataSynchronizer {
        +synchronizeTimestamps(dataList: List~SensorData~)
        +calculateOffset(deviceTime: Long, referenceTime: Long)
        +alignSensorStreams(streams: Map~String, Stream~)
    }
    
    class SessionManager {
        +createSession(config: SessionConfig)
        +addDevice(device: SensorDevice)
        +startRecording()
        +stopRecording()
        +exportData(format: ExportFormat)
    }
    
    class NetworkController {
        +establishConnection(endpoint: String)
        +sendCommand(command: Command)
        +receiveData(): SensorData
        +handleDisconnection()
    }
    
    ThermalProcessor --> SessionManager: reports to
    GSRRecorder --> SessionManager: reports to
    DataSynchronizer --> SessionManager: used by
    NetworkController --> SessionManager: communicates with
    SessionManager --> DataSynchronizer: coordinates
```

## 🔌 Hardware Integration

### Supported Thermal Cameras

```mermaid
graph LR
    subgraph "USB Connected"
        TC001[TC001 Series]
        HIK[HIKVision Cameras]
    end
    
    subgraph "Wireless Connected"  
        TC007[TC007 Wireless]
        TS004[TS004 Network]
    end
    
    subgraph "Processing Modules"
        ThermalIR[thermal-ir]
        ThermalLite[thermal-lite]
        HIKLib[libhik]
    end
    
    TC001 --> ThermalIR
    TC007 --> ThermalIR
    TS004 --> ThermalLite
    HIK --> HIKLib
    
    ThermalIR --> Analysis[Thermal Analysis]
    ThermalLite --> Analysis
    HIKLib --> Analysis
```

### BLE Sensor Integration

The `BleModule` provides:
- Shimmer3 GSR sensor connectivity
- Real-time physiological data streaming
- Data synchronization with thermal capture
- Multi-sensor coordination

## 🧪 Testing

### Unit Tests
```bash
# Run all tests
./gradlew test

# Test specific module
./gradlew :component:thermal-ir:test

# Test with coverage
./gradlew testDebugUnitTestCoverage
```

### Integration Tests
```bash
# PC Controller tests
cd pc-controller
python -m pytest test_system_integration.py

# Comprehensive tests
python test_comprehensive.py
```

## 📚 Comprehensive Documentation

### 🚀 Getting Started
- **[Quick Start Guide](docs/QUICK_START.md)** - Essential setup and usage
- **[User Manual](docs/USER_MANUAL.md)** - Complete user documentation
- **[Troubleshooting](docs/TROUBLESHOOTING.md)** - Common issues and solutions

### 🏗️ Architecture & Development
- **[Developer Guide](docs/DEVELOPER_GUIDE.md)** - Development procedures and architecture  
- **[Architecture Guide](docs/ARCHITECTURE.md)** - Detailed system architecture
- **[Contributing Guide](docs/CONTRIBUTING.md)** - Contribution guidelines and standards

### 📖 Technical References
- **[Technical Specifications](docs/TECHNICAL_SPECIFICATIONS.md)** - Complete technical specifications for all components
- **[API Reference](docs/API_REFERENCE.md)** - Basic protocol and SDK documentation
- **[Advanced API Documentation](docs/ADVANCED_API_DOCUMENTATION.md)** - Comprehensive API with detailed examples

### 🧩 Component Documentation
- **[Thermal-IR Module](docs/modules/THERMAL_IR_MODULE.md)** - Primary thermal imaging component
- **[GSR Recording Module](docs/modules/GSR_RECORDING_MODULE.md)** - Shimmer3 GSR sensor integration
- **[LibIR Library](docs/modules/LIBIR_LIBRARY.md)** - Core thermal processing algorithms
- **[PC Controller](docs/modules/PC_CONTROLLER.md)** - Python-based central hub

### 📊 Additional Resources
- **[Performance Benchmarks](docs/PERFORMANCE_BENCHMARKS.md)** - System performance analysis
- **[Security Guidelines](docs/SECURITY_GUIDELINES.md)** - Security implementation guide
- **[Deployment Guide](docs/DEPLOYMENT_GUIDE.md)** - Production deployment instructions

## 🤝 Contributing

We welcome contributions to the IRCamera platform:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/thermal-enhancement`)
3. **Commit** your changes (`git commit -m 'Add thermal enhancement feature'`)
4. **Push** to the branch (`git push origin feature/thermal-enhancement`)
5. **Open** a Pull Request

### Contribution Guidelines

- Follow existing code style and patterns
- Add tests for new functionality
- Update documentation as needed
- Ensure all builds pass before submitting PR

See **[CONTRIBUTING.md](docs/CONTRIBUTING.md)** for detailed guidelines.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Topdon Technology** for thermal camera hardware and SDK support
- **HIKVision** for enterprise thermal camera integration
- **Shimmer Research** for GSR sensor integration and physiological sensing
- **Android Community** for CameraX and modern Android development patterns
- **Open Source Community** for various libraries and tools used in this project

---

**IRCamera** - Advanced Thermal Imaging Platform  
*Professional thermal imaging with multi-device support and advanced analysis capabilities*