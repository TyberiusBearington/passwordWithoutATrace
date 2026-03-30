# Password Without A Trace

Secure, self-destructing password sharing with client-side encryption.

## How it Works
1.  **Encryption**: The password is encrypted in your browser using AES-GCM 256-bit encryption. The server only sees and stores the encrypted data.
2.  **Zero-Knowledge**: The decryption key is included in the URL fragment (after the `#`). Since fragments are never sent to the server, the server has no way to decrypt your password.
3.  **Self-Destruction**: You can set a limit on the number of views and an expiration time. Once either limit is reached, the data is permanently deleted from the Redis database.

## Running Locally with Docker

You can run the entire stack (Application + Redis) using Docker Compose.

### Basic Run
```sh
docker-compose up --build
```
Access the app at: `http://localhost:8080`

### Running with ngrok (Public URL)
To share passwords with someone outside your local network, you can use ngrok to create a secure tunnel.

1.  Sign up for a free account at [ngrok.com](https://ngrok.com).
2.  Copy your **Authtoken** from the ngrok dashboard.
3.  Run the application with your token:

**PowerShell (Windows):**
```powershell
$env:NGROK_AUTHTOKEN="YOUR_TOKEN_HERE"; docker-compose up --build
```

**Command Prompt (Windows):**
```cmd
set NGROK_AUTHTOKEN=YOUR_TOKEN_HERE && docker-compose up --build
```

**Bash (Linux/macOS):**
```bash
NGROK_AUTHTOKEN=YOUR_TOKEN_HERE docker-compose up --build
```

4.  Find your public URL by:
    *   Checking the terminal logs.
    *   Opening the ngrok dashboard at: `http://localhost:4040`

## Development
*   **Backend**: Spring Boot 3.2.5
*   **Database**: Redis
*   **Frontend**: Vanilla HTML/JS with Web Crypto API
*   **Build Tool**: Gradle 8.5
