# Bookshelf Architecture

## System Overview

The Bookshelf application follows a layered architecture pattern with clear separation of concerns. The application is built using Spring Boot and follows MVC (Model-View-Controller) architectural pattern.

```
┌─────────────────────────────────────────────────────────────┐
│                      Presentation Layer                      │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │   Thymeleaf │    │  TypeScript │    │    Static   │     │
│  │  Templates  │    │     App     │    │   Assets    │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
├─────────────────────────────────────────────────────────────┤
│                      Web Layer (Controllers)                 │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │    Auth     │    │    Book     │    │   Kitchen   │     │
│  │ Controller  │    │ Controller  │    │  Controller │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
├─────────────────────────────────────────────────────────────┤
│                      Service Layer                          │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │    User     │    │  Kitchen    │    │  Security   │     │
│  │  Service    │    │   Table     │    │  Service    │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
├─────────────────────────────────────────────────────────────┤
│                      Repository Layer                       │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │    Book     │    │    User     │    │   Table     │     │
│  │ Repository  │    │ Repository  │    │ Repository  │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
├─────────────────────────────────────────────────────────────┤
│                      Data Layer                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                    H2 Database                      │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Component Details

### 1. Presentation Layer
- **Thymeleaf Templates**: Server-side rendered views
- **TypeScript Application**: Client-side interactivity and state management
- **Static Assets**: CSS, images, and other static resources

### 2. Web Layer (Controllers)
- **AuthController**: Handles user authentication and registration
- **BookController**: Manages book-related operations (CRUD)
- **KitchenTableController**: Manages virtual kitchen table interactions
- **LandingController**: Handles the main landing page

### 3. Service Layer
- **UserService**: User management and registration logic
- **KitchenTableService**: Kitchen table seat management
- **CustomUserDetailsService**: Spring Security user authentication

### 4. Repository Layer
- **BookRepository**: Book entity persistence
- **UserRepository**: User entity persistence
- **TableSeatRepository**: Kitchen table seat persistence

### 5. Data Layer
- H2 in-memory database
- JPA entities and relationships

## Key Components and Their Responsibilities

### Authentication Flow
```
┌──────────┐     ┌───────────────┐     ┌─────────────────┐
│  Client  │ ──▶ │ AuthController│ ──▶ │  UserService    │
└──────────┘     └───────────────┘     └─────────────────┘
                                              │
                                              ▼
                                    ┌─────────────────────┐
                                    │ UserDetailsService  │
                                    └─────────────────────┘
```

### Book Management Flow
```
┌──────────┐     ┌───────────────┐     ┌─────────────────┐
│  Client  │ ──▶ │BookController │ ──▶ │ BookRepository  │
└──────────┘     └───────────────┘     └─────────────────┘
```

### Kitchen Table Flow
```
┌──────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Client  │ ──▶ │KitchenController│ ──▶ │KitchenService   │
└──────────┘     └─────────────────┘     └─────────────────┘
                                               │
                                               ▼
                                     ┌─────────────────────┐
                                     │ TableSeatRepository │
                                     └─────────────────────┘
```

## Security Architecture

1. **Authentication**
   - Form-based authentication
   - BCrypt password encryption
   - Session-based user tracking

2. **Authorization**
   - URL-based security rules
   - Method-level security
   - User-specific data access control

## Data Model

```
┌─────────────┐      ┌─────────────┐      ┌─────────────┐
│    User     │      │    Book     │      │  TableSeat  │
├─────────────┤      ├─────────────┤      ├─────────────┤
│ id          │──┐   │ id          │      │ id          │
│ username    │  │   │ title       │      │ seatNumber  │
│ password    │  └──▶│ author      │      │ userId      │
│ avatar      │      │ pages       │      └─────────────┘
└─────────────┘      │ summary     │
                     │ review      │
                     │ position    │
                     │ userId      │
                     └─────────────┘
```

## Frontend Architecture

The frontend is built with TypeScript and uses a modular approach:

```
src/main/resources/
├── static/
│   ├── ts/
│   │   ├── app.ts          # Main application entry
│   │   ├── books/          # Book management
│   │   └── kitchen-table/  # Kitchen table features
│   ├── css/
│   └── images/
└── templates/              # Thymeleaf templates
    ├── layout/
    ├── books/
    └── kitchen-table/
```

## Build and Deployment

The application uses a multi-stage build process:

1. **Backend Build (Gradle)**
   - Compilation
   - Test execution
   - JAR packaging

2. **Frontend Build (Webpack)**
   - TypeScript compilation
   - Asset bundling
   - Resource optimization

## Future Considerations

1. **Scalability**
   - Database migration to a persistent solution
   - Caching layer implementation
   - Microservices architecture consideration

2. **Performance**
   - Frontend optimization
   - Database query optimization
   - Asset delivery optimization

3. **Features**
   - Reading progress tracking
   - Book recommendations
   - Social features expansion 