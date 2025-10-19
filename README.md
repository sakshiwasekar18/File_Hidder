File Hidder is a console-based Java application that allows users to securely hide and unhide files using a MySQL database.
It features user authentication, file encryption, and OTP-based verification, making it a simple but powerful security utility.
Setup & Installation

Clone this repository:

git clone https://github.com/<your-username>/File_Hidder.git
cd File_Hidder


Create a MySQL database:

CREATE DATABASE filehidder;
USE filehidder;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE
);

CREATE TABLE data (
    id INT PRIMARY KEY AUTO_INCREMENT,
    filename VARCHAR(255),
    filepath TEXT,
    email VARCHAR(100),
    bin_data LONGBLOB
);


Update your MySQL credentials in:

src/main/java/org/example/db/MyConnection.java


Example:

private static final String URL = "jdbc:mysql://localhost:3306/filehidder";
private static final String USER = "root";
private static final String PASSWORD = "yourpassword";


Build and run the project:

mvn clean install
java -cp target/classes org.example.Main

🧑‍💻 How It Works

Run the program — you’ll see the main menu.

Choose Signup to register a new user.

After signup, login with your email.

Choose to hide a file — it gets encrypted and deleted from your system.

Later, unhide it to recover the original file.
![File Hidden Screenshot](https://github.com/user-attachments/assets/8c75412f-2a32-4507-9641-8b353df2ed77)

