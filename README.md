# Bookshelf

A modern web application that allows users to manage their personal book collection and interact with other readers in a virtual kitchen table environment.

## Features

- 📚 Personal Book Management
  - Add, edit, and remove books from your collection
  - Track book details including title, author, pages, summary, and personal review
  - Organize books with drag-and-drop positioning

- 🪑 Virtual Kitchen Table
  - Join other readers at an 8-seat virtual kitchen table
  - Real-time seat management
  - Social interaction space for book lovers

- 🔐 User Authentication
  - Secure user registration and login
  - Personalized avatars
  - Protected user data and book collections

## Technology Stack

- Backend:
  - Java 21
  - Spring Boot 3.2.2
  - Spring Security
  - Spring Data JPA
  - H2 Database

- Frontend:
  - TypeScript
  - Thymeleaf templates
  - Webpack for asset bundling

## Prerequisites

- Java 21 or higher
- Node.js and npm (for frontend development)

## Setup and Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/bookshelf.git
   cd bookshelf
   ```

2. Build the backend:
   ```bash
   ./gradlew build
   ```

3. Install frontend dependencies:
   ```bash
   npm install
   ```

4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

The application will be available at `http://localhost:8080`

## Development

- Backend development server: `./gradlew bootRun`
- Frontend development: `npm run watch`
- Run tests: `./gradlew test`

## Database

The application uses H2 in-memory database by default. The H2 console is available at `http://localhost:8080/h2-console` when running in development mode.

## Security

- CSRF protection is disabled for simplicity in development
- Passwords are encrypted using BCrypt
- Session-based authentication
- Protected API endpoints

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
