# Rency
This application uses an open api to fetch currency rates and enables you to easily do conversion between them. By entering an amount for some currency will reflect the converted amount in the others.

## Decisions

Given the size of the application and it's limited goal for scalability I have implemented the app in a pragmatic fashion; not going overkill with gradle modules, inheritance, interfaces.
This does give implications when it comes to Clean Architecture, SOLID Principle and testability- things I would want to see in a real application. But for a work sample and to give an insight I figured this was enough.

The architecture is my interpretation of a quick/pragmatic MVVM explained below. It can be scaled up to follow Clean/SOLID but the work required is not feasible for this size.

I have chosen to use Data Binding, because it is in my opinion the best thing ever to happen to Android. Ok, maybe not true but I love using it and it enables me create powerful views with massive reusability. I think that Data Binding shines in two aspects: When using lists, and when you have a complex screen. Lists are easily implemented and hooked up where its needed, and screens get very clean with easy to follow!

DataBinding disclaimer: I have been working with DataBinding commercially since it was experimental and it's extremely powerful, but also really easy to abuse! You need to have some rules in place to not create blindness and confusion; Such as "no logic in xml"!

### Screen: Coordinator Pattern
I've choosen an MVVM architecture, but with some modification. I came to a conclusion about what I believe is the responsibility of the Activity, ViewModel and View. I have created a pattern I feel is very powerful in its simplicity that separates logic to their bare concerns, I call it the Coordinator Pattern. With a quick description: 
- The Activity is not the View, it's a lifecycle and platform handler. It handles communication with the Android/Context, Navigation and the Lifecycle.
- The ViewModel only holds view logic (i.e not any rxCalls, disposables, holding on and modifying data). It will bind to the xml and hold resources needed for it.
- The Coordinator is the controller part of the MVVM, the logic part that MVVM puts in the ViewModel. It will be observed by the activity, instruct the ViewModel and call the Interactor for contact with the Repository.
- In this instance the screen is a list so it will hold an adapter that supports data-binding. Those ViewDataModels (really, ViewModels, but Google stole that term) have a direct connection with the Coordinator and are sent to the ViewModel to be displayed.
- The Interactor "interacts" with Repositories and handles logic tied to them.

### Clean Architecture
This is a basic implementation of a clean architecture with immutable data and directional dependencies, but like I've explained above I've cut some pragmatic corners making a perfect implementation not possible.

### Testing
I have put in some test but they are somewhat limited by the structure and because of the amount of work to setup a refined test suite.

##### Implemented 17 July, 2020
