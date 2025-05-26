-- Create milestones table
CREATE TABLE milestones (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    actual_date DATE NOT NULL
);

-- Create games table
CREATE TABLE games (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create game_milestones table (junction table)
CREATE TABLE game_milestones (
    id BIGSERIAL PRIMARY KEY,
    game_id BIGINT NOT NULL REFERENCES games(id) ON DELETE CASCADE,
    milestone_id BIGINT NOT NULL REFERENCES milestones(id),
    correct_order INTEGER NOT NULL,
    UNIQUE(game_id, milestone_id),
    UNIQUE(game_id, correct_order)
);

-- Create game_attempts table
CREATE TABLE game_attempts (
    id BIGSERIAL PRIMARY KEY,
    game_id BIGINT NOT NULL REFERENCES games(id),
    player_name VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('IN_PROGRESS', 'COMPLETED')),
    attempt_count INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

-- Create indexes for performance
CREATE INDEX idx_games_slug ON games(slug);
CREATE INDEX idx_game_milestones_game_id ON game_milestones(game_id);
CREATE INDEX idx_game_milestones_correct_order ON game_milestones(game_id, correct_order);
CREATE INDEX idx_game_attempts_game_id ON game_attempts(game_id);
CREATE INDEX idx_game_attempts_status ON game_attempts(status);
CREATE INDEX idx_milestones_actual_date ON milestones(actual_date);

-- Insert sample milestones for testing
INSERT INTO milestones (title, description, actual_date) VALUES
('Moon Landing', 'Apollo 11 lands on the Moon', '1969-07-20'),
('Fall of Berlin Wall', 'The Berlin Wall is torn down, symbolizing the end of the Cold War', '1989-11-09'),
('First iPhone Released', 'Apple releases the first iPhone, revolutionizing mobile technology', '2007-01-09'),
('World Wide Web Invented', 'Tim Berners-Lee creates the first web browser and web server', '1990-12-20'),
('Titanic Sinks', 'RMS Titanic sinks on its maiden voyage', '1912-04-15'),
('Declaration of Independence Signed', 'American colonies declare independence from Britain', '1776-07-04'),
('Printing Press Invented', 'Johannes Gutenberg invents the printing press', '1440-01-01'),
('Discovery of Penicillin', 'Alexander Fleming discovers penicillin', '1928-09-28'),
('First Computer Bug', 'Grace Hopper finds the first computer bug', '1947-09-09'),
('DNA Structure Discovered', 'Watson and Crick discover the double helix structure of DNA', '1953-04-25'),
('First Television Broadcast', 'John Logie Baird makes the first television broadcast', '1926-01-26'),
('Steam Engine Invented', 'James Watt patents the steam engine', '1769-01-05'),
('Theory of Relativity Published', 'Einstein publishes his theory of special relativity', '1905-09-26'),
('First Flight', 'Wright brothers achieve first powered flight', '1903-12-17'),
('Internet Created', 'ARPANET, precursor to the Internet, goes online', '1969-10-29');

