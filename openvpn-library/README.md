# ICS OpenVPN Library

A ready-to-use Android library (AAR) built from the popular [ICS OpenVPN](https://github.com/schwabe/ics-openvpn) project, packaged for easy integration via JitPack.

## Features

- 🔒 **OpenVPN 2 and OpenVPN 3 Support** - Choose between two flavors
- 📦 **AAR Format** - Easy integration as Android library
- 🚀 **JitPack Distribution** - Simple dependency management
- 🔧 **Ready to Use** - Pre-compiled with all native dependencies
- 📱 **Android 21+** - Supports Android 5.0 and above

## Usage with JitPack

Add the JitPack repository to your root `build.gradle` file:

```gradle
allprojects {
    repositories {
        // ... other repositories
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your app module's `build.gradle` file:

### OpenVPN 3 variant (recommended):
```gradle
dependencies {
    implementation 'com.github.nizwar:ics-openvpn:TAG'
}
```

### OpenVPN 2 variant:
```gradle
dependencies {
    implementation 'com.github.nizwar:ics-openvpn-v2:TAG'
}
```

Replace `TAG` with the latest release tag or commit hash.

## Features

- **Two OpenVPN variants**: Choose between OpenVPN 2 and OpenVPN 3
- **Full Android support**: Native libraries for all Android architectures
- **Complete VPN functionality**: All features from the original ICS OpenVPN app
- **Easy integration**: Use as a library in your own Android applications

## Architecture Support

The library includes native binaries for:
- ARM64 (arm64-v8a)
- ARMv7 (armeabi-v7a) 
- x86
- x86_64

## Requirements

- Android API 21+ (Android 5.0+)
- AndroidX support

## License

This project maintains the same license as the original ICS OpenVPN:
- GNU General Public License v2.0
- See [LICENSE.txt](doc/LICENSE.txt) for full terms

## Original Project

This library is based on [ICS OpenVPN by Arne Schwabe](https://github.com/schwabe/ics-openvpn).

## Building

To build the library yourself:

```bash
git clone https://github.com/nizwar/ics-openvpn.git
cd ics-openvpn
git submodule update --init --recursive
./gradlew :openvpn-library:assembleRelease
```

The AAR files will be generated in `openvpn-library/build/outputs/aar/`.
