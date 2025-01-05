-- Create default user only if it doesn't exist
MERGE INTO USERS KEY(username) VALUES (
    'demo',
    '$2a$10$8GxHr0a7i5.g4XL3Nv9gYeGzxTd.qXB5tUX6q3ODyZKDNxEK8NCJC',
    '📚'
);

-- Insert books only if they don't exist
MERGE INTO BOOK KEY(title, user_id) VALUES (
    'The Great Gatsby',
    'F. Scott Fitzgerald',
    180,
    'Set in the summer of 1922 on Long Island, New York, The Great Gatsby follows narrator Nick Carraway as he encounters the mysterious millionaire Jay Gatsby and becomes entangled in Gatsby''s pursuit of his former love, Daisy Buchanan.',
    'A masterpiece of American literature that captures the essence of the Jazz Age. The prose is beautiful and the story is both tragic and compelling.',
    0,
    (SELECT id FROM USERS WHERE username = 'demo')
);

MERGE INTO BOOK KEY(title, user_id) VALUES (
    '1984',
    'George Orwell',
    328,
    'Set in a totalitarian future society, follows Winston Smith, a man who secretly rebels against the government by keeping a diary of his forbidden thoughts and falls in love with Julia, a fellow rebel.',
    'A chilling and prophetic novel about surveillance, control, and the destruction of truth. More relevant today than ever.',
    1,
    (SELECT id FROM USERS WHERE username = 'demo')
);

MERGE INTO BOOK KEY(title, user_id) VALUES (
    'To Kill a Mockingbird',
    'Harper Lee',
    281,
    'Through the eyes of Scout Finch, we see her father Atticus defend a black man accused of a terrible crime in a small Southern town. A story about racial injustice and the loss of innocence.',
    'A powerful and compassionate portrayal of racial injustice and moral growth. Lee''s characters are unforgettable, especially Atticus Finch.',
    2,
    (SELECT id FROM USERS WHERE username = 'demo')
);

MERGE INTO BOOK KEY(title, user_id) VALUES (
    'The Hobbit',
    'J.R.R. Tolkien',
    310,
    'Bilbo Baggins, a comfort-loving hobbit, is swept into an epic quest to reclaim the lost Dwarf Kingdom of Erebor from the fearsome dragon Smaug, guided by the wizard Gandalf.',
    'A charming and adventurous tale that set the stage for The Lord of the Rings. Perfect blend of fantasy, adventure, and humor.',
    3,
    (SELECT id FROM USERS WHERE username = 'demo')
); 