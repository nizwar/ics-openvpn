# ICS OpenVPN Library

A ready-to-use Android library (AAR) built from the popular [ICS OpenVPN](https://github.com/schwabe/ics-openvpn) project, packaged for easy integration via JitPack.

## Features

- 🔒 **OpenVPN 3 Support** - Modern OpenVPN 3 C++ library implementation
- 📦 **AAR Format** - Easy integration as Android library
- 🚀 **JitPack Distribution** - Simple dependency management
- 🔧 **Ready to Use** - Pre-compiled with all native dependencies
- 📱 **Android 21+** - Supports Android 5.0 and above

## Installation

### Step 1: Add JitPack Repository

Add JitPack to your project's `settings.gradle.kts` (or `settings.gradle`):

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // Add this line
    }
}
```

### Step 2: Add Dependency

Add the dependency to your app's `build.gradle.kts` (or `build.gradle`):

```kotlin
dependencies {
    // ICS OpenVPN Library (OpenVPN 3)
    implementation("com.github.nizwar:ics-openvpn:v1.0.0")
}
```

## Usage

### Basic Setup

```kotlin
import de.blinkt.openvpn.core.ProfileManager
import de.blinkt.openvpn.core.VpnStatus
import de.blinkt.openvpn.core.VPNLaunchHelper
import de.blinkt.openvpn.VpnProfile

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize ProfileManager
        val profileManager = ProfileManager.getInstance(this)
        
        // Listen to VPN status changes
        VpnStatus.addStateListener { state, logMessage, localizedResId, level, connectedProfile ->
            // Handle VPN state changes
        }
    }
}
```

### Loading and Starting VPN Profile

```kotlin
// Load VPN profile from .ovpn file
val vpnProfile = ProfileManager.getInstance(this).getProfileByName("your-profile-name")

// Or create from config string
val configString = "your-ovpn-config-content"
val vpnProfile = VpnProfile.getProfile("profile-name", configString)

// Start VPN connection
VPNLaunchHelper.startOpenVpn(vpnProfile, this)
```

### Permissions

Add required permissions to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

## Architecture Support

The library includes native binaries for:
- ARM64 (arm64-v8a)
- ARMv7 (armeabi-v7a) 
- x86
- x86_64

## Build Information

- **Android Gradle Plugin**: 8.7.3
- **Kotlin**: 2.1.0
- **Target SDK**: 35 (Android 14)
- **Min SDK**: 21 (Android 5.0)
- **Java Version**: 17

## Building from Source

To build the library yourself:

```bash
git clone https://github.com/nizwar/ics-openvpn.git
cd ics-openvpn
git submodule update --init --recursive
./gradlew :openvpn-library:assembleOvpn23Release
```

The AAR file will be generated in `openvpn-library/build/outputs/aar/`.

## Version History

- **v1.0.0** - Initial release with OpenVPN 3 support

## Contributing

This library is built from the upstream [ICS OpenVPN](https://github.com/schwabe/ics-openvpn) project. For OpenVPN-related issues, please refer to the original repository.

For library packaging and distribution issues, please open an issue in this repository.

## License

This project maintains the same license as the original ICS OpenVPN:
- GNU General Public License v2.0
- See [LICENSE.txt](doc/LICENSE.txt) for full terms

## Credits

- Original ICS OpenVPN project by [Arne Schwabe](https://github.com/schwabe)
- OpenVPN community for the excellent VPN software
- JitPack for seamless library distribution
