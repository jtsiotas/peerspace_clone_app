-- Insert roles
INSERT INTO roles (name)
VALUES
    ('ADMIN'),
    ('HOST'),
    ('GUEST');


INSERT INTO capabilities (name, description)
VALUES
    -- Admin specific
    ('MANAGE_USERS', 'Full access to manage all users'),
    ('MANAGE_PROPERTIES', 'Full access to manage all properties'),
    ('MANAGE_BOOKINGS', 'Full access to manage all bookings'),
    ('MANAGE_REVIEWS', 'Full access to manage all reviews'),
    
    -- General User Capabilities
    ('VIEW_OWN_PROFILE', 'View own user profile details'),
    ('EDIT_OWN_PROFILE', 'Modify own user profile'),
    ('DELETE_OWN_PROFILE', 'Remove own user profile'),
    ('VIEW_USERS', 'View user profiles (for hosts to see guests and vice versa)'),
    
    -- Property Capabilities
    ('INSERT_PROPERTY', 'Create a new property'),
    ('VIEW_PROPERTIES', 'View property list and details'),
    ('EDIT_OWN_PROPERTY', 'Modify own property'),
    ('DELETE_OWN_PROPERTY', 'Remove own property'),
    
    -- Booking Capabilities
    ('INSERT_BOOKING', 'Create a new booking'),
    ('VIEW_OWN_BOOKINGS', 'View own bookings (as guest or host)'),
    ('EDIT_OWN_BOOKING', 'Modify or cancel own booking'),
    
    -- Review Capabilities
    ('INSERT_REVIEW', 'Create a new review'),
    ('VIEW_REVIEWS', 'View reviews'),
    
    -- Message Capabilities
    ('SEND_MESSAGE', 'Send a message'),
    ('VIEW_MESSAGES', 'View own messages');


INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
CROSS JOIN capabilities c
WHERE r.name = 'ADMIN';


INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
JOIN capabilities c ON c.name IN (
    'VIEW_OWN_PROFILE', 'EDIT_OWN_PROFILE', 'DELETE_OWN_PROFILE', 'VIEW_USERS',
    'INSERT_PROPERTY', 'VIEW_PROPERTIES', 'EDIT_OWN_PROPERTY', 'DELETE_OWN_PROPERTY',
    'VIEW_OWN_BOOKINGS', 'EDIT_OWN_BOOKING',
    'INSERT_REVIEW', 'VIEW_REVIEWS',
    'SEND_MESSAGE', 'VIEW_MESSAGES'
)
WHERE r.name = 'HOST';


INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
JOIN capabilities c ON c.name IN (
    'VIEW_OWN_PROFILE', 'EDIT_OWN_PROFILE', 'DELETE_OWN_PROFILE', 'VIEW_USERS',
    'VIEW_PROPERTIES',
    'INSERT_BOOKING', 'VIEW_OWN_BOOKINGS', 'EDIT_OWN_BOOKING',
    'INSERT_REVIEW', 'VIEW_REVIEWS',
    'SEND_MESSAGE', 'VIEW_MESSAGES'
)
WHERE r.name = 'GUEST';
