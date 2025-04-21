CREATE TABLE favorites (
    id INT IDENTITY(1,1) PRIMARY KEY,
    member_id INT NOT NULL,
    attraction_id INT NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),

    CONSTRAINT FK_Favorites_Member FOREIGN KEY (member_id)
        REFERENCES members(member_id),

    CONSTRAINT FK_Favorites_Attraction FOREIGN KEY (attraction_id)
        REFERENCES attractions(id)
);
