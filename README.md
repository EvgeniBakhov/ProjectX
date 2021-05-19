### PROJECT X

This project is a diploma work for the university. This app is created for searching for events and estates. 

Project architecture - MVC. 

Used technologies and plugins:
- spring boot
- spring security
- swagger
- lombok plugin
- hibernate

For persisting the data in the app is used mySQL database and hibernate framework.
Repositories:
- UserRepository - allows us to operate the data related to users.
- EstateRepository - for persisting and reading the data about estates.
- EventRepository - for persisting and reading the data about events.
- PasswordTokenRepository - persists password reset token for reset password functionality.
- PrivilegeRepository - persists user's privileges (for each role).
- RoleRepository - persists roles, that user could have.

Service layer.

Services contain all business logic for data validation and data processing. They are also playing role of proxy 
for transferring data between persisting layer and controllers.

- BookingService - in this service implemented functionality for booking an estates.
- EstateService - contains business logic related to estates.
- EventService - business logic for events, such as publish an event, update event details and others.
- UserService - implementation of business logic as a user registration, updating and retrieving user data.

Controllers: 

This layer is to expose an API to provide access to service logic for other apps (in case frontend Angular app).

Roles and privileges system:

Each user has only one role: NORMAL, OWNER, ORGANIZER or ADMIN. Each role has a set of privileges. Privilege - string,
that represents activity allowed by this privilege.

### Version 0.12.0
Now booking can be approved by an owner of an estate.

### Version 0.11.0
Added service method for getting bookings for current user.

### Version 0.10.0
Added logic for getting bookings for a specific estate.

### Version 0.9.0
Added logic for getting booking by id.

### Version 0.8.0
Added an endpoint for booking cancelling.

### Version 0.7.0
Added update booking endpoint. Added comment to booking.

### Version 0.6.1
Existing bookings validation added.

### Version 0.6.0
Endpoint for getting event by an id.

### Version 0.5.0
Endpoints for event update and deletion added. Add a picture to profile.

### Version 0.4.0
Estate deletion implemented.

### Version 0.3.0
Added endpoint for estate update and assemblers.

### Version 0.2.0
Added model, service and controller for reservation.

### Version 0.1.0
Implemented basic functionalities of the app such as: user registration, user login, estate and event publishing.
