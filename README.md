# Therapeutic

## Apk - https://github.com/develNerd/Therapeutic/blob/main/androidApp/release/androidApp-release6.apk

Therapeutic is a Kotlin Mobile Multiplatform App that aims to help make access to mental health solutions as effortless as we can. We believe that making therapy an easy to have will help reduce the replications of mental health issues. The USA for example has had multiple mass shootings, studies have shown that most of the culprits may have suffered some form of mental health issue. 

Studies have shown that one in five (5) people may experience mental health issues at some point in their life. Mental health issues might not only have an effect on the patient, as he interacts with others this might extend to damage their relationships with others as well as extend to people they interact with.



We also aim to provide and extend our services to the third world, as at these parts of the world, therapy is taken very lightly and a very small portion of people value the need for therapy. 

If we could start off as a completely non-profit company, we might just have volunteer therapists who would love to work with us to make the world a better place.

## What it does
Therapeutic uses a mobile application as it's main channel for initial user interactions. It has the following as it's main functions.

1. The app provides video content having genres that relate to how our users can reduce or deal with mental health situations. These vides include videos on meditation, self awareness etc.

2. Online mental health education events are organized and made available to the users through links on the apps homepage. The events can be joined using various social media channels, such as Twitter (Spaces), Zoom, Google meet etc. The app aims to make Twitter Spaces the main forum for such events.

3. A chat bot: A friendly chatbot named **Euti** provides users with a way to easily utilize the app by making navigation easy. Euti is there to provide a form of in person interaction to our users.

**The following are some features that are specific to Euti**

- Schedule or Book sessions with our therapists (Volunteer therapists or or employees)

- Users can also reschedule or cancel appointments.

- Easily sort events for ongoing and upcoming events, for users to easily join a session.

- Provide users with podcasts that help to reduce mental stress as the user listens to them (We make sure, such consumable contents are good enough for the user).

## How we built it

The software currently uses **Kotlin** as it's main programming language and leverages on the following frameworks, architectures and libraries.

- Kotlin Multiplatform Mobile (KMM). We wanted to provide our users with the best experience as possible as such, we made the app native two both mobile platforms i.e Android and IOS. The project in it's current stage has the Android side completed for an MVP. 

- As we know, users of such platforms need the best of experience and should be encouraged to continue using it, we have our engineers build the project following best practices for our architecture and code base. (Proofed in the codebase link, NB: Tests are yet to be written) 

- We leveraged on the native capabilities of Kotlin and have made the features of the app as robust as possible. 

- Our database for hosting events, videos and podcasts use firebase firestore, as our authentication system also relies on firebase auth.

- Our booking feature leverages Squares booking API. We have also made use of the following square APIs 
   - Customer API
   - Team member API (To help manage our employees i.e therapists in this case)
   - Employee API
   


## Challenges we ran into
1. Our first caveat was to find a suitable social media platform, large enough to accommodate thousands of people around the world that are interested in participating in our online events. Google meet, Zoom and others have known limits to the amount of people that can join a session at a time.
We have however decided to go with twitter spaces for now and can support other platforms by changing the links of the **Events** object.

2. Finding good content is also a difficult task, as we would want to provide uses with content that would help them in mild or acute phases of their disease.

3. One main technical challenge was the learning curve on building the app with Kotlin Multiplatform Mobile. We knew it's advantages but we also knew the risks involved as it's an upcoming framework and has not matured enough as the other frameworks. As a matter of fact it's still in beta state.

4. It's also currently a one man team and therefore we're looking a bring investors and shareholders on board :).

## Accomplishments that we're proud of
1. Our premier feature is our chatbot : **Euti** , we are proud of the effort put in to make euti a good companion for our users. With euti we can also easily scale our features as we aim to make it's features as dynamic as possible, meaning we also want to easily add new features without an update to the app bundle.

2. We will feel more accomplished if our app serves it's purpose to help reduce replications of mental health issues, even below a 15% success rate at it's early stages. 

## What we learned

1. Well, we learnt a lot about mental health in general and it's effects on our friends, family, co-workers and anyone we relate with in one way or the other.

2. We've learnt a lot technically as we've explored new frameworks in the mobile development field.

## What's next for Therapeutic

Therapeutic will wish to start as a non-profit company in order to attract investors and professional therapists willing to make our world a better place. We will also build forums to solicit for donations.

If our MVP is successful hopefully, we shall start business early 2023 (in 1st Quarter).

## Screen Shots





![Therape-min](https://user-images.githubusercontent.com/37780207/186479789-5443161a-f578-4bc1-97fc-6fb45aed653a.png)  ![therapeutic_username-min](https://user-images.githubusercontent.com/37780207/186479883-c453e41c-dae6-4f98-a4cf-4251300886e9.png)  ![therapeutic_home-min](https://user-images.githubusercontent.com/37780207/186480371-2be77e35-f369-40cc-b9bf-c40efb3a780a.png)  ![therapeutic_euti-min](https://user-images.githubusercontent.com/37780207/186480656-dd132a08-44f8-48e4-adbb-e30e3f0d6f55.png)  ![therapeutic_euti_home-min](https://user-images.githubusercontent.com/37780207/186480725-a5650add-cfe4-4ecd-98bf-649ae0e716b1.png)  ![therapeutic_euti_upcoming-min](https://user-images.githubusercontent.com/37780207/186480765-49fa1e62-1c32-4f7b-8e3f-3fa22c5f0d29.png)  ![therapeutic_join_event-min](https://user-images.githubusercontent.com/37780207/186480794-08494619-95be-4cea-8d0e-d9c48bbf6035.png)  ![therapeutic_event_scduled-min](https://user-images.githubusercontent.com/37780207/186481179-876ec8de-6b49-4e1b-8fc3-e6ba16b7fe14.png)




