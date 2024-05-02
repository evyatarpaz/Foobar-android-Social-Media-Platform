
# Welcome to foobar Android App! 📱

Foobar Android App is the latest and most advanced social network application! In this version, we have created the layout for the sign-up page, sign-in page, and the post feed.

Registration is mandatory to access the application. Users must fill in all fields during registration, and the password must meet specific criteria: it should contain at least one lowercase letter, one uppercase letter, one number, and be at least 8 characters long. Additionally, users are required to upload a profile image.

This project implements a RESTful API using NodeJS, designed to interface with both an Android application and a React web application. It uses MongoDB for data persistence and supports user authentication via JWT.

### Server Setup:

- **Technology Stack** : NodeJS, Express, MongoDB
- **Security** : JWT for authentication
- **API Base URL** : [local](http://10.0.2.2:8080/api/)

## Authentication
### User Registration
- `POST /api/users` - Registers a new user with username, password, display name, and profile picture.
### User Login
- `POST /api/tokens` - Logs in a user and returns a JWT.

## API Endpoints
### Users
- **Create User**: `POST /api/users`
- **Get User Details**: `GET /api/users/:id`
- **Update User Details**: `PUT /PATCH /api/users/:id`
- **Delete User**: `DELETE /api/users/:id`

### Posts
- **Fetch Posts**: `GET /api/posts` - Returns the latest 20 posts of friends and 5 latest posts of non-friends.
- **Create Post**: `POST /api/users/:id/posts`
- **Update Post**: `PUT /PATCH /api/users/:id/posts/:pid`
- **Delete Post**: `DELETE /api/users/:id/posts/:pid`

### Friends
- **Get Friends List**: `GET /api/users/:id/friends`
- **Add Friend**: `POST /api/users/:id/friends`
- **Confirm Friend Request**: `PATCH /api/users/:id/friends/:fid`
- **Delete Friend**: `DELETE /api/users/:id/friends/:fid`

## Error Handling
Responses from the server will include appropriate status codes:
- `200 OK` - Request successful.
- `404 Not Found` - The requested resource was not found.
- `409 Conflict` - Registration attempt with an existing username.

### Features:

- **Sign-Up Page** 📝: Allows users to register for a new account by filling out a registration form.

- **Sign-In Page** 🔐: Provides users with a form to sign in to their accounts.

- **Post Feed** 📰: Displays a dynamic feed with a list of 10 posts and images loaded from a local JSON file. Users can upload posts, comment on posts, and manage their own content by editing or deleting posts and comments.

  - Each post has options to like, comment, and share.
  
  - Clicking on the "Write a comment" button reveals all comments for that post.
  
  - The settings button on each post allows users to delete or edit their posts.

---

## Project Overview


### SignUp Screen

- The SignUp screen allows users to register for a new account by filling out a registration form.

![Screenshot 2024-03-07 222649](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/88116ec0-3d54-478c-8b71-19fc493940d2)

- If the password entered in the "Password" and "Confirm Password" fields does not match, an error message is displayed indicating that the passwords do not match.
 
![Screenshot 2024-03-07 224121](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/50fcda43-ba7e-43e3-bd0c-ca10960bc1f6)

- Dark mode

![Screenshot 2024-03-07 230852](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/f0fefabe-e2b5-474d-8f17-e531fbcf6ce7)

- Users must fill out all the fields in the registration form to proceed with the sign-up process.

![Screenshot 2024-03-07 222808](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/5054f2e2-19f9-4d00-9bff-162f23f96fd7)

- Users are required to upload a profile picture during the registration process.

![Screenshot 2024-03-07 222756](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/4f59eadc-3c5f-46b1-a37e-235ff739128c)

- Password must contain at least one uppercase lowercase and a number, the password must be at least 8 characters

![Screenshot 2024-03-07 222718](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/4b0d564c-9f0c-4fd5-9b8f-30d71bc04a9c)

- Upload a profile photo, gallery or camera

![Screenshot 2024-03-07 222831](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/d4411154-7abe-4b1a-9267-68277535120e)


- "Click here" link at the bottom of the screen will navigate the user to the SignIn screen.

![Screenshot 2024-03-07 222657](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/737b3e0f-bfc8-4186-b411-58c5ae97826c)



### SignIn Screen

- The SignIn screen provides users with a form to sign in to their accounts.

![Screenshot 2024-03-07 222633](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/b9d75de9-39ab-4e55-a705-02317bf93688)

- If the entered password is incorrect, an error message is displayed indicating that the password is incorrect.

![Screenshot 2024-03-07 222911](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/a9d79da0-e959-44cc-ad8e-dd2605107e4d)

- Dark mode
  
![Screenshot 2024-03-07 222640](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/b1f49cd6-2713-4e3f-b386-7b3614de971b)
  
- The "Click here" link at the bottom of the screen will navigate the user to the SignUp screen to create a new account.

![Screenshot 2024-03-07 222640](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/f0a997c7-7b39-4889-812f-49b14e3dc796)



### Post Feed

- The Post Feed displays a dynamic list of posts uploaded by users.
  ![image](https://github.com/Eliaddr119/Foobar-part2-Android/assets/114868880/93e20a64-a5d9-49b9-85cc-45c36d45aaba)
- By dragging down you can fetch the latest 20 posts from friends and 5 from non-friends.
  ![image](https://github.com/Eliaddr119/Foobar-part2-Android/assets/114868880/b2d1e8e5-9c55-45a6-832e-e63bfc71b0d9)

- By clicking on the option button on the upper right side users can edit and delete their posts
![image](https://github.com/Eliaddr119/Foobar-part2-Android/assets/114868880/59d42f09-2f83-4050-92d9-c83f54aaee54)

- Users can add a new post by clicking on the "add a new post" button.

![Screenshot 2024-04-30 105814](https://github.com/Eliaddr119/Foobar-part2-Android/assets/114868880/89bf38a4-9124-48d7-93c2-7e22ee2721b6)
 * return using the return button on your android phone
![Screenshot 2024-03-07 223129](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/7d835e73-5c63-4561-a721-1bc31ac652f4)

- Users can access additional settings, including dark mode and logout, by clicking on the settings icon.

![image](https://github.com/Eliaddr119/Foobar-part2-Android/assets/114868880/d521965b-b7ac-4c8d-b799-78b0fab5550f)
- Update Profile allows you to change the user's display name and password, and by pressing the photo above you can change the profile picture. You can also delete the account from here
  ![image](https://github.com/Eliaddr119/Foobar-part2-Android/assets/114868880/e6c27b54-6135-4c26-bf44-33a1290582a9)

- Users can write a comment on a post by clicking on the comment button

![Screenshot 2024-03-07 231048](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/26f3363e-97f7-4d08-a193-cab45cbd1f37)

![image](https://github.com/Eliaddr119/Foobar-part2-Android/assets/114868880/9a2b4492-bb42-4a3e-b01e-31cd3dc60b14)

- Users can like a post by clicking on the like button
![Screenshot 2024-04-30 110113](https://github.com/Eliaddr119/Foobar-part2-Android/assets/114868880/d990a7a3-6014-4bb0-ab3d-a8c71b0c5b1f)

- Users can open the share menu by clicking on the share button
  
![Screenshot 2024-03-07 233136](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/b499a832-b0be-431a-9310-b382680908a2)

![Screenshot 2024-03-07 223031](https://github.com/Eliaddr119/Foobar-part2-Android/assets/113431442/68c1a3c0-d8d6-4de6-9f84-1796763363a2)

- Users can see if they have friend requests by clicking the friend request button
  ![Screenshot 2024-04-30 110625](https://github.com/Eliaddr119/Foobar-part2-Android/assets/114868880/58351a3d-a154-47b5-918b-2ad269814a56)

- Users can press on the display name of other users in the post feed and get into the user's profile, if they are friends they can see each other posts.
  ![image](https://github.com/Eliaddr119/Foobar-part2-Android/assets/114868880/2e805492-2338-4e49-a9d3-05ac919a29ac)
- By pressing the friend request button on the user's post you can friend and unfriend them
  ![Screenshot 2024-04-30 111022](https://github.com/Eliaddr119/Foobar-part2-Android/assets/114868880/df3d01e1-0ea1-4b27-a9a4-b4505de09548)

---

### Minimum API Level:

- API Level 34

---

## Note

This version of the foobar Android app operates entirely within the app itself. All functionalities, including user registration, posting, and commenting, are handled within the app.

The post feed displays a list of 10 posts with images loaded from a local JSON file. Users can interact with these posts, including uploading new posts, commenting on existing posts, and managing their own content by editing or deleting posts and comments.

Please note that the minimum API level required for the app is API Level 34.

