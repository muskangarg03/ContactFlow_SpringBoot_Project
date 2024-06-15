# Smart Contact Manager

The Smart Contact Manager project is a comprehensive web application designed to streamline contact management for individuals and businesses. This application provides a secure, efficient, and user-friendly platform for managing contacts.

## Table of Contents
- [Modules](#modules)
  - [User Management](#user-management)
  - [Contact Management](#contact-management)
- [Technology Stack](#technology-stack)
- [Hardware and Software Requirements](#hardware-and-software-requirements)
  - [Hardware Requirements](#hardware-requirements)
  - [Software Requirements](#software-requirements)
- [Implemented Features](#implemented-features)
- [Implementation Methodology](#implementation-methodology)
- [Workflow](#workflow)
  - [Model](#model)
  - [View](#view)
  - [Controller](#controller)
  - [Database Interaction](#database-interaction)
  - [Deployment](#deployment)
- [Dependencies Used](#dependencies-used)
- [CI/CD Implementation](#cicd-implementation)
- [Quick Start Guide](#quick-start-guide)




## Modules

### User Management
- **Register**: Users can create accounts by providing necessary details, creating a secure profile within the system.
- **Login**: Secure authentication ensures authorized access for registered users.
- **Logout**: Users can securely log out, terminating their session.

### Contact Management
- **Show Contacts**: Users can view a comprehensive list of their contacts, organized for easy access.
- **Update Contacts**: Modify existing contact details to keep information current.
- **Add Contacts**: Easily add new contacts, including essential information such as names, phone numbers, and email addresses.
- **Search Contacts**: A robust search functionality allows users to quickly find specific contacts within their database.


## Technology Stack

- **Spring Boot**: Foundation framework for back-end development.
- **Spring Framework**: Used for Spring MVC (web development) and Spring Security (authentication and authorization).
- **Spring Thymeleaf**: Template engine for dynamic HTML rendering.
- **MySQL**: Backend database for storing and retrieving contact information.
- **AWS (Amazon Web Services)**: Cloud platform for CI/CD, EC2 for deployment.
- **HTML5, CSS3, Bootstrap**: Front-end development.
- **Java, JavaScript, SQL**: Core programming languages.


## Hardware and Software Requirements

### Hardware Requirements
- **Operating System**: Platform Independent
- **RAM**: 1GB (min)
- **Hardware Devices**: Desktops, Tablets, Mobiles
- **Hard Disk**: 30GB

### Software Requirements
- **Frameworks**: Spring Boot, Spring MVC, Spring Security, Spring JPA, Hibernate
- **Languages**: Java, JavaScript, HTML, SQL
- **Template Engine**: Thymeleaf
- **Database**: MySQL
- **Cloud**: AWS
- **VCS**: Git/GitHub
- **Automation Tools**: Docker, Jenkins, Maven
- **Deployment**: AWS
- **Server**: Apache-Tomcat9
- **Web Browser**: All existing browsers
- **IDE**: VSCode, Spring Tool Suite, Eclipse


## Implemented Features
- **Clean URLs**: Enhanced user experience through intuitive navigation.
- **Validations**: Ensures data integrity and consistency through robust validation mechanisms.
- **Role-Based Authorization using Spring Security**: Secure access control based on user roles.
- **Encrypted Passwords**: Enhanced security with encrypted password storage.
- **Pagination**: Efficient handling of large datasets with pagination functionality.
- **Security Checks for Contact Viewing**: Ensures users can only view permitted contacts.
- **Automatic Data Deletion**: Maintains data integrity and storage efficiency by deleting images from the file system upon removal from the application.
- **Active Links**: Enhances user engagement with clickable and responsive links.
- **Email Verification**: Ensures account security and authenticity through email verification mechanisms.
- **OTP Validation**: Additional security layer with OTP validation for user password recovery.
- **Responsive Design**: Optimized for seamless access across various devices.
- **Dynamic Titles**: Customized and dynamic page titles for improved user experience.
- **Twilio Call Service**: Integrates Twilio's communication APIs for voice call functionality within the application.
- **Compose Mail Feature**: Allows users to compose and send emails directly from within the application.
- **CI/CD Implementation**: Automates the build, test, and deployment processes with CI/CD pipelines.


## Implementation Methodology

The application follows the Model View Controller (MVC) architecture, which is divided into three main components:
- **Model**: Responsible for maintaining data.
- **View**: Responsible for displaying all or a portion of the data to the user.
- **Controller**: Manages the interaction between the Model and View.

This pattern isolates business logic from the user interface and supports the separation of concerns.

## Workflow

### 1. Model
- **Entity Classes**: Entity classes, such as `Contact` and `User`, represent the core data structure for contacts and users. These classes are annotated with JPA annotations for database mapping.
- **Repositories**: Spring Data JPA repositories handle database operations for the entities. They provide methods for CRUD (Create, Read, Update, Delete) operations.

### 2. View
- **Thymeleaf Templates**: Thymeleaf templates are used for creating dynamic HTML pages. These templates can include Thymeleaf expressions to display data retrieved from the backend.
- **Static Resources**: CSS, JavaScript, and images are organized in static resource directories. These resources enhance the user interface and contribute to a visually appealing design.

### 3. Controller
- **Controllers**: Controllers, annotated with `@Controller`, handle incoming HTTP requests. Each method in a controller is annotated with `@RequestMapping` or other annotations to specify the URL mapping.

### 4. Database Interaction
- **JPA-Hibernate**: Manages the interaction with the MySQL database. Entity classes are mapped to database tables, and Spring Data JPA repositories provide an abstraction for database operations.
- **MySQL**: Used to store and retrieve contact information. It provides a reliable and structured storage solution for the application.

### 5. Deployment
- **Packaging**: The application will be packaged into a JAR or WAR file using Maven.
- **Deployment**: The packaged application can be deployed to a servlet container or a cloud platform, such as AWS. Deployment configurations and properties are often specified in `application.properties` or `application.yml`.


## Dependencies Used

- **Web Development**: `starter-web`, `starter-thymeleaf`
- **Data Access**: `starter-data-jpa`, `mysql-connector-java`
- **Security**: `starter-security`
- **Development Tools**: `spring-boot-devtools`
- **Validation**: `validation-api`, `hibernate-validator`
- **Communication Integration**: `javax.mail`, `twilio`


## CI/CD Implementation

A CI/CD (Continuous Integration/Continuous Deployment) pipeline automates the integration and deployment of software changes to ensure seamless integration and delivery.

### Steps to Implement CI/CD Pipeline
**1.Code Development and Version Control (Git):** Write application code and store it in a version control system like Git.

**2.Continuous Integration (CI):**
- Trigger: Push or merge to the repository triggers the CI pipeline.
- Jenkins: Monitors the Git repository for changes, pulls code from the repository, and uses Maven for build automation.
- Maven: Compiles source code, manages dependencies, runs tests, and packages the application.
- Build: Jenkins builds the application and generates artifacts (JAR or WAR files).

**3.Dockerization:**
- Docker: Containerizes the application with its dependencies.
- Dockerfile: Defines the environment and steps to run the application.
- Container Registry: Pushes Docker images to a registry like DockerHub.

**4.Continuous Deployment (CD):**
- AWS EC2 Instance Configuration: Set up EC2 instance with necessary configurations.
- Deployment to EC2: Jenkins deploys the Docker image to the AWS EC2 instance via SSH.
- Docker Installation: Install Docker on the EC2 instance.
- Docker Container Deployment: Jenkins runs the Docker container on the EC2 instance, exposing necessary ports for application access.


## Start Guide
 **1. Clone the repository**
   ```bash
   git clone https://github.com/your-repository/smart-contact-manager.git
```

**2. Navigate to Project Directory**

**3. Build Project Using Maven**

**4. Run the application**

**5. Acess the application**
Open your web browser and navigate to http://localhost:9000.
