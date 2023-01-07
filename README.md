# Agile Software Development Practices (SOFT2412/COMP9412)
CC_08_Thu_08_ Sudeshna_Group-1
- School of Computer Science
- Group Project Assignment 2 – Agile Software Development with Scrum and Agile Tools
---------------------------------------------------------
- Work: Group assignment (4-5 students)
- Weight: 25%
- Due: Multiple deadlines (see submission requirements below)
- Deliverables: Multiple technical reports, source code submissions, and project demonstrations (detailed below)
---------------------------------------------------------
***TO RUN THE PROGRAM***
> gradle run
---------------------------------------------------------

# Background

- The goal of this assignment is to work as a team on developing a software product using the Scrum method and Agile development tools and practices.
- In this software development project, your team must apply **Scrum practices and principles** properly to build a software product in [JAVA].
- Each team also has to use the **Agile software** tools they set-up during the first group project to following CI/CD practices in their development.
- These tools must be set up and ready **before** writing the code of your application.
- Each team will have a **stakeholder** or client/customer (your tutor) for their project.
- Your team will have the opportunity to explore and asks question related to software requirements from the project client during the lab/tutorial time (your tutor will act as a client whom you should satisfy in terms of delivering a working version of the software).

- As a team, you will get hands-on experience on how to organise and conduct Agile software development using the Scrum method and Agile development tools. Specifically, you and your team will be able to:
    - Setup and use Agile development tools and CI/CD practices,
    - Experience the importance of Agile values,
    - Understand and apply Scrum roles,
    - Write and estimate User Stories,
    - Plan and execute development Sprints,
    - Conduct Scrum events,
    - Continuously improve the development process,
    - Produce various Scrum Artifacts.

# Forming Scrum Teams

- In this group project, you will continue to work with the team from Agile Development Tools project.
- So, it's crucial to reflect on team dynamics and difficulties and think as a team how to collaborate and work in more productive environment.
- The team structure must follow Scrum method which should help your team to be more self-organized. Your team need to decide on the following roles:
    - 1 Product Owner (PO),
    - 1 Scrum Master (SM), and
    - 2-3 Core Team members.
    - After team formation, each team will need to discuss and agree on the above Scrum roles.
    - Both SM and PO have also to contribute to development (writing code using CI/CD) in all Sprints, in which they need to develop parts of the software product.


---------------------------------------------------------
> The following are the details/requirements of the software that you need to develop in this project.
---------------------------------------------------------

# Vending Machine (Lite Snacks) Software

> Lite Snacks manage vending machines which sell different types of snacks.

- The snacks are maintained under **four** different categories:
    1. drinks
    2. chocolates
    3. chips
    4. lollies.

- [done] Each category will have different items/products.
- [done] The vending machine can maintain up to 15 items of each product.
- [done] The following is the product items for each category (the quantity is set to 7 items at the first ran:
    - Drinks: Mineral Water, Sprite, Coca cola, Pepsi, Juice.
    - Chocolates: Mars, M&M, Bounty, Snickers.
    - Chips: Smiths, Pringles, Kettle, Thins.
    - Candies: Mentos, Sour Patch, Skittles.

- [done] By default, the application will show the **list product options** which are **grouped** based on the **categories** that can be **selected by customers**.
- A customer can select one item or more from the list of available products and specify number of items they want to purchase.
- The customer can also **cancel** the transaction anytime (e.g. by clicking a cancel button) and any **idle** activity that is more than 2 minutes (timeout) will automatically cancel the transaction.
    - The cancelled transaction will return the application to the **default page** (list of products).

- [done] The **default** page has a button to login/create account by providing **username** and **password**.
    - The **username** must be **unique** and the **password** input is hidden by showing **asterisk** symbol (*).
    - After the **customer** **creates** the account for the first time, it will be **automatically** logged in.
    - When the user is **logged in**, the default page will show a list of the **last 5 products/items** bought by this user.
        - Otherwise (when anonymous user), the default page will show list of last bought 5 products/items bought by anonymous users.


---------------------------------------------------------
## Payment.Payment

> There are **two** payment methods are allowed:

### Payment.Cash:
- The customer will require to input the quantity of coins and/or notes that are valid in Australia (e.g. $5, $10, $20, 5c, 10c, etc.).
- If the money provided by the customer is **not enough** to complete the transaction,
    - then they should be prompted to **enter the remaining mount** or **cancel the transaction**.
- Upon **successful** transaction, a customer receives the select products and correct change.
    - The change must be optimal where higher notes or coins are prioritised. For example, if the customer uses a $50 note for a $5 item, the changes will be two $20 notes and one $5 note (not four $10 notes and five $1 coins).
    - However, you must consider quantity of the available coins or notes inside the vending machine when calculating the change.
    - For example, if there is only one $20 note, then the changes for the previous example will be one $20 note, two $10 notes and one $5 note.
    - If there is no possible change available for given cash money provided by the customer, you need to **notify the customer** that there is no available change with the inserted money.
    - You can ask the customer to insert/use different notes/coins to complete the payment or cancel the transaction.
    - At the **first** run, you can set the availability of each note and each coin to 5.

### Payment.Card:
- The customer will require to provide "cardholder name" and "credit card number".
- You only need to check whether the provided credit card details match with any of this credit card list: credit_cards.json, and notify users when it is invalid (no match in the list).

- A logged in user will be prompted an **option to save** this credit card detail when the transaction is completed and successful.
    - When it is saved and the customer does another transaction next time, the credit card details will be automatically filled in.
    - Please note that the card number input will **hidden** with asterisk (*) as well.

- When the transaction is complete, the product stock will be updated accordingly and any logged in customer will be logged out automatically. Any cancelled transaction (both due to timeout or pressing cancel button in any page state) will log out the customer as well.
---------------------------------------------------------
## Roles
> Beside customer, there are three other roles/type of user available to use the vending machine:

### Seller:
- This role is able to **fill/modify** the item details.
- To fill and modify the items, 
  - this role is able to select and modify item details such as 
    - item name
    - item code
    - item category
    - item quantity
    - item price.

- Appropriate error message must be shown if the quantity added will be over than the limit (15 of each item/product) or there is conflicting item code/name/category.
- This role will also able to obtain two reports (either csv or text file) upon logged in:
    - A list of the current available items that include the item details.
    - A summary that includes items codes, item names and the total number of quantity sold for each item (e.g. "1001; Mineral Water; 12", "1002; Mars; 1", etc.).

### Cashier:
- This role is able to fill/modify the available change or notes/coins available in the vending machine.
- This role will be able to modify quantity of each note and each coin available in the vending machine.
- This role will also able to obtain two reports:
    - A list of the current available change (the quantity of each coin and each note in the vending machine).
    - A summary of transactions that includes transaction date and time, item sold, amount of money paid, returned change and payment method.
      - item / cost / payment method. <- reason: how much stock / profit the machine made.
      - 

### Owner:
- This role is able to add/remove Seller or Cashier or Owner user(s).
- In addition, this role can modify the items details and the available changes in the vending machine as well.
- Please note that a user cannot have more than one role, e.g. a customer user cannot have Seller role.
- This role should be able to generate all of above reports with two additional reports:
    - A list of usernames in the vending machine with the associated role (customer, seller, cashier, or admin).
    - A summary of cancelled transaction.
    - This summary only includes date and time of the cancelled, the user (if available, otherwise "anonymous") and the reasons (e.g. "timeout", "user cancelled", "change not available", etc.).
    -
---------------------------------------------------------
- The application design can be using any App.UI (text or GUI) for the functionality.
- You can decide on the application design/architecture; both text file or persistent database are acceptable.
- The software must always produce correct output and maintain correct and consistent state of all included entities.

> Like the first group project, the team’s development work must be under the SOFT2412 GitHub organization accounts. Your tutor must have access and can inspect the source code at any point of time. Your tutor should also be able to inspect your CI/CD used in the development.

---------------------------------------------------------

# Scrum – The Project Sprints

- You have a total of 4 weeks to complete your project.
- The project development will be divided into project setup and group preparation.
- The actual development of the Vending Machine software must run over **3 Sprints** (iterations).
    - Each Sprint should last for one week.
    - Each Team need to conduct all Scrum events during each Sprint.
    - The first Sprint must start from your week 10's tutorial.
    - Each team must follow the Sprint schedule as described below:

        - Preparation from week 9.
            - Your team can use this period to decide team roles, get more understanding with the Scrum method and to make appropriate development setup (e.g., agile development tools) and to prepare initial user stories (including initial product backlog) based on the above described requirements.
        - Sprint 1: starts week 10 (on your tutorial day) and ends week 11 (on your tutorial day)
        - Sprint 2: starts week 11 (on your tutorial day) and ends week 12 (on your tutorial day)
        - Sprint 3: starts week 12 (on your tutorial day) and ends week 13 (on your tutorial day)
        - From week 9 tutorial, your team will have an opportunity to clarify and elaborate on the Vending Machine system requirements.

- During each [Sprint], your team must:

    - Carry out the development work following Scrum practices and principles, and CI/CD practices using the agile development tools.
    - Document all the Scrum events that happens, **user stories**, relevant team members **interactions** and **resulted/updated artifacts**. This also includes the tools used to implement Scrum.
    - Document the key events that occurred during the development of the Vending Machine system.
        - This includes the use of Agile development tools and practices (including CI/CD) by all team members.
        - For the daily stand-up meeting, please adjust the meeting into fours times in a week and you can use Zoom instead of face to face meeting.


- At the end of each Sprint, your team must:
    - Conduct the Sprint review during the tutorial and demonstrate the current version (increment) of the Vending Machine system to your client (tutor).
    - Your tutor will provide feedback about the product and make some observations about the demo from the client's point of view (client role).
    - The tutor will also note down their own obserations about each demo. and how 's the work conducted using Agile tools and Scrum (tutors role).
    - These will be used to assess the project demos. and the technical and non-tchncial work conducted across the 3 Sprints by the group and the individuals.
    - Submit a group Sprint report that document all the work done in that Sprint as per the project requirements.
    - Submit the version of the source code demonstrated to the client.

---------------------------------------------------------

# Building the Vending Machine Software using the Agile Tools

Each team must develop the Vending Machine software application in JAVA. All team members must collaboratively develop the application’s requirements using Agile development tools and practices. Each team must use the provided GitHub organization with their Unikeys. You may creat new repository but make sure your tutor can access and inspect.  Each team must produce a version of the Vending Machine software at the end of each Sprint and ensure that:

The program must always produce correct output based on the requirements (based on this assignment description) and additional requirements from your client/tutor.
The program must be built in a modular way since every team member are required to contribute on writing the code. For this assignment, the security aspect can be neglected so encryption is not needed to store the password or credit card details.
Each team must carry on the development using Scrum method and all the tools and practices (CI/CD) that have been used in the first group project. Each team is required to demonstrate the proper use of these tools. Automated build and unit test must be triggered successfully with appropriate test/code coverage and reporting. You must make sure your unit tests have a good code coverage (>75% code coverage). In this project, it's acceptable to use Jenkins polling feature to trigger automated build and tests,
 
---------------------------------------------------------


# Submission Requirements

## Weekly Demo

Each team must demonstrate to their client (tutor) a version of their Vending Machine software at the end of each Sprint in their scheduled tutorial. Each team member must demo their individual contributions (user stories / code tasks) they complete in each Sprint to their client. In the GitHub account, your team must label the source code (git tag) that will be demonstrated to the client with appropriate version number so your tutor can inspect the appropriate source code. In the last Sprint, each team must demonstrate the complete Vending Machine software. The Sprint’s demos are due as follows:

Sprint 1 Demo: Week 11’s tutorials (all group members must participate)
Sprint 2 Demo: Week 12’s tutorials (all group members must participate)
Sprint 3 Demo: Week 13’s tutorials (all group members must participate)  
All group members must  attend all the Sprint Demos and participate in the demonstration. These weekly demos will be assessed by your tutor from both client point of view as well as how Scrum and Agile development tools were used in each Sprint. Your tutor will also observe the invidual conributions based on the presented user stories, Scrum development, and use of Agile tools.

## Weekly Report and Source Code

Each team must submit a report at the end of each Sprint starting from end of Sprint 1 (week 11 on your tutorial day) through provided links on Canvas (one submission per group).

The report should include concise documentation of the team’s development of their Vending Machine software  using Scrum and the Agile development tools and practices as detailed in sections 4 and 5. Note your team should provide all scrum events, scrum artifacts, and evidences to support the description and justification of the development work presented in their report. The source code of each Sprint must be also submitted through the submission links to be provided on Canvas. Besides, each group has to submit an executable of their final source code (week 13's Sprint) along with instructions how to run the code for offline testing. The Sprint’s reports and source code are due as follows:

Sprint 1 Report &  Source Code: Week 11 at 23:59 on the day of your scheduled tutorial (separate link will be provided under assignments)
Sprint 2 Report & Source Code: Week 12 at 23:59 on the day of your scheduled tutorial (separate link will be provided under assignments)
Sprint 3 Report & Source Code: Week 13 at 23:59 on the day of your scheduled tutorial (separate link will be provided under assignments)
All group members must sign the assignment coversheetDownload assignment coversheet and attach it as the first page of each report. Contribution issues must be reported early (in any of the Sprints) to your tutor so they can investigate it.



# Group Member Contribution

If members of your group do not contribute sufficiently (in any of the Sprints) you must alert your tutor within 2 days from the start of each Sprint. Each team member must be assigned user stories/features to implement during the Sprint planning and everyone's progress must be tracked through the project tracking tools (Github, Trello/JIRA). The course instructor has the discretion to scale the group’s mark for each member based on their contributions as follows:

Level of Contribution	Proportion of final grade received
No contribution	0%
poor/partial contribution 	1% - 49%
Partial/poor contribution	50%-99%
Full contribution	100%


Marking Guide
The marking of each iteration will be based on the requirements detailed above.  Below is a summary of the marking guide which will be used along with the detailed requirements listed above.

1. Quality of Scrum Development (50%)

Project Team (5%)

Quality of team organization and roles in accordance with Scrum method and as detailed in the above assignment requirements.
This must be evident by the project documentation and demonstrations
Sprint Goal (5%)

Quality of Sprint goal; sensible, relevant, and clear Sprint goals
This must be evident by project documentation and demonstrations.
Tasks Board (Product and Sprint) (10%)

Quality of the way the team created, managed, and maintained the Product and Sprint Backlogs in accordance with Scrum and as detailed in the above assignment requirements (correctness, completeness, relevance, sensible and clear).
This must be evident by project documentation and demonstrations.
Scrum Events Artifacts (30%)

Quality of the way the team carried out, managed, and maintained Scrum events and artifacts in accordance to Scrum and as detailed in the above assignment requirements (correctness, completeness, relevance, sensible and clear).
This should include all Scrum events and artifacts as well as challenges/issues faced by the team during the Sprint and how it’s resolved.
This must be evident by project documentation and demonstrations.


2. Agile Development Tools and Practices (15%)

Quality of the application and use of CI/CD tools and practices (GitHub, Gradle, Junit and Jenkins) in the weekly Scrum development work as a team.
Working source code must be on Github with appropriate labels/tags/releases of each end of Sprint release. Final delivery of source code must also include how-to guide.
This must be evident by project documentation and demonstrations.


3. Quality of Application Development (20%)

Clear and sensible explanation of the work carried out by the team (what, how and why) to implement the requirements of the Ticket Management Application. This should be demonstrated through the project report and group demo including:
Description about how the group collaborated to complete the application using the above tools set up, individual and group contributions, group communication (you can reference other sections/parts if already presented previously in your report)
Overview of the Ticket Management Application design (e.g., class diagrams, sequence diagrams, or any format)
How well the Ticket Management Application produces correct output/behaviour with various inputs and adheres to the functional requirements during the demo (recorded and the live Q&A)
All the above must be supported by appropriate evidences where applicable (e.g., screenshots, outputs, logs).


4. Quality of Demonstration (15%)

Quality of the demonstrated version of the software from the client point of view including producing correct output and behaviour based on the initial and additional requirements as elaborated by the client during the Sprint. The application produces correct output during the demonstration and Q&A session (including running and testing the submitted code where required)
Demonstrate how the demoed version/product meets the client’s needs and delivers high quality (well-tested and working without any errors, exceptions, mistakes, or deviation from core requirements)
The team is able to sensibly respond to client questions and requests, explain and justify the work they’ve done in the project, and the results produced during the demo and Q&As.

5. Quality of individual contribution

Each team member made fair and enough contribution to the development of the Vending Machine software application using Scrum and Agile tools (as per the assignment requirements). All members collaborated and worked as a team. This is clearly evident by the GitHub repository and tools (e.g., Trello, JIRA) and provided evidence in each of the above development activities.
Each team member should clearly indicate their contribution in the report (table of roles and contributions and reference to the sections in the report).
The weekly demos will be assessed by your tutor from both client point of view as well as how Scrum and Agile development tools were used in each Sprint. Your tutor will also observe the invidual conributions based on the presented user stories, Scrum development, and use of Agile tools.
The mark for each member will be scaled based on their contributions to the project as per the contribution percentages described in the assignment requirements (0%-100%)

Note: Assignment updates will be made on this page and announced on Ed.  