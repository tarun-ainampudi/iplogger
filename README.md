# iplogger

A simple Android application that tracks the IPv4 address assigned to a device on a Wi-Fi network.

The app was built for networks that use IP-based authentication or session management. Since DHCP servers may assign a different IP address over time, `iplogger` keeps a history of previously assigned addresses so users can identify old IPs and manage active sessions when needed.

## How It Works

1. The app detects Wi-Fi connection changes.
2. It reads the device's current IPv4 address.
3. The IP address is stored locally in a rolling history.
4. The app maps the IP address to a block based on predefined subnet ranges.
5. A background job periodically records the current IP address.

## Features

* Automatically logs IPv4 addresses on Wi-Fi connections
* Stores recent IP history locally
* Maps IP addresses to blocks
* Periodically records IP changes in the background
* Persists data across app restarts

## Block Mapping

| Third Octet | Block |
| ----------- | ----- |
| 32 - 63     | A-1  |
| 64 - 95     | A-2  |
| 96 - 127    | B-1    |
| 128 - 135   | B-2  |

## Build and Run

Clone the repository:

```bash
git clone https://github.com/tarun-ainampudi/iplogger.git
```

Open the project in Android Studio and run it on a physical Android device connected to Wi-Fi.

## Project Structure

```text
com.iplogger/
├── MainActivity.java
├── Helper.java
└── IPLoggerJobService.java
```

## Requirements

* Android SDK (API 21+)
* ACCESS_WIFI_STATE permission
* ACCESS_NETWORK_STATE permission
* Android device with Wi-Fi connectivity

```
> Note: Wi-Fi connection events are best tested on a physical device rather than an emulator.
```

## License

This project is licensed under the MIT License.
