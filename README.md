# Volunteering Backend

A Spring Boot backend application for managing volunteering opportunities. This service provides a RESTful API for managing volunteer opportunities, user registrations, and related functionalities.

## 🚀 Features

- User authentication and authorization
- CRUD operations for volunteering opportunities
- File upload functionality
- Email notifications
- WebSocket support for real-time updates

## 📋 Prerequisites

- Java 21
- Maven 3.8+
- PostgreSQL database
- Docker (optional)

## 🛠️ Setup and Installation

1. **Clone the repository**
   ```bash
   git clone [repository-url]
   cd volunteering-backend
   ```

2. **Configure environment variables**
   Create a `.env` file in the root directory with the following variables:
   ```properties
   DB_PASSWORD=your_database_password
   # Add other environment variables as needed
   ```

3. **Build the application**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   
   Or using Docker:
   ```bash
   docker-compose up
   ```

The application will be available at `http://localhost:8080`

## 📚 API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Main Endpoints

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate user
- `GET /api/opportunities` - List all volunteering opportunities
- `POST /api/opportunities` - Create a new opportunity
- `GET /api/opportunities/{id}` - Get specific opportunity details
- More endpoints available in Swagger documentation

## 🧪 Testing

Run the tests using:
```bash
mvn test
```

For test coverage report:
```bash
mvn verify
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Coding Standards

- Follow Java coding conventions
- Write unit tests for new features
- Update documentation as needed
- Use meaningful commit messages

## 📄 License

This project is licensed under the [License Name] - see the LICENSE file for details

## 👥 Authors

- Your Name - Initial work

## 🙏 Acknowledgments

- List any acknowledgments or third-party resources used 