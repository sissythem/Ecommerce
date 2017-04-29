# Ecommerce
\documentclass[12pt]{article}
\usepackage[english]{babel}
\usepackage{natbib}
\usepackage{url}
\usepackage[utf8x]{inputenc}
\usepackage{amsmath}
\usepackage{graphicx}
\graphicspath{{images/}}
\usepackage{parskip}
\usepackage{fancyhdr}
\usepackage{vmargin}
\setmarginsrb{3 cm}{2.5 cm}{3 cm}{2.5 cm}{1 cm}{1.5 cm}{1 cm}{1.5 cm}

\title{Android Project}   
 % Title
\author{Vassiliki Moschou, M1507 \\
	Chrysoula Themeli, M1423}                               % Author
\date{\today}                                           % Date

\makeatletter
\let\thetitle\@title
\let\theauthor\@author
\let\thedate\@date
\makeatother

\pagestyle{fancy}
\fancyhf{}
\rhead{\theauthor}
\lhead{\thetitle}
\cfoot{\thepage}

\begin{document}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\begin{titlepage}
    \centering
    \vspace*{0.5 cm}
    \includegraphics[scale = 0.75]{ekpalogo.png}\\[1.0 cm]   % University Logo
    \textsc{\LARGE National and Kapodistrian University of Athens}\\[2.0 cm]   % University Name
    \textsc{\Large Department of Informatics and Telecommunicatios}\\[0.5 cm]               % Course Code
    \textsc{\large E-Commerce Technologies}\\[0.5 cm]               % Course Name
    \rule{\linewidth}{0.2 mm} \\[0.4 cm]
    { \huge \bfseries \thetitle}\\
    \rule{\linewidth}{0.2 mm} \\[1.5 cm]
    
    \begin{minipage}{0.4\textwidth}
        \begin{center} \large
            \theauthor
            \end{center}
            \end{minipage}~
            \begin{minipage}{0.4\textwidth}
    \end{minipage}\\[2 cm]
    
    {\large \thedate}\\[2 cm]
 
    \vfill
    
\end{titlepage}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\tableofcontents
\pagebreak

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\section{Introduction}
For the requirements of the course of E-Commerce Technologies, an Android Application for booking rooms/residences, similar to Airbnb Application, was implemented. This report fully analyzes the design decisions, the assumptions and all necessary details related to the project. 

The progress of technology has completely changed the values of commerce... %introduction, description of e-commerce 
The purpose of this project is to bring closer users and hosts... %purpose of application

%Connection Details

\section{Installation}

\subsection{MySQL Database}
For the successful operation of the application, seven tables have been created: users, residences, rooms, reservations, reviews, conversations and messages. 

\begin{itemize}
	\item \textbf{Users: }The fields of this table describe all necessary information for all the users, both tenants and hosts. The mandatory fields, in order the user to be valid, are: first name, last name, username, password and email. However, there are extra information that can be filled out later by the user. In this table, we also store the information of the role of each user (host role enabled or not).
	\item \textbf{Residences: }In this table all the residences, entered by hosts, are described. Specifically, the information stored is: the owner, the type (house, apartment or single room), location details, short description and characteristics, cancellation policy, rules, available dates and price information of the residence. 
	\item \textbf{Rooms: }Since the residences may have more than one rooms for booking, due to different types as described above, several users should have the opportunity to make a reservation during the same period. Because of this need, a table named "rooms" was created, where more specific information is contained, such as number of beds, available bathrooms and more. The field "residence\_id" connects each room with its parent residence (foreign\_key).
	\item \textbf{Reservations: }
\end{itemize}



\bibliographystyle{plain}
\bibliography{biblist}

\end{document}
