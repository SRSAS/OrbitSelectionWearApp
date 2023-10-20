# OrbitSelectionWearApp
WearOS App for XR orbit selection. Has 2 screens, one for IP selection, other to swipe, sends swipe coordinates through UDP socket.
## Authorship
Project authored by **Sebastião Andrade e Sousa**

[LinkedIn](https://www.linkedin.com/in/sebasti%C3%A3o-andrade-e-sousa-700827270/) -- [GitHub](https://github.com/SRSAS)


Developed during an internship researching subtle interactions with mobile XR for [HCI Lab @ IST](https://web.tecnico.ulisboa.pt/augusto.esteves/)

Project supervised by Professor [Augusto Esteves](http://web.tecnico.ulisboa.pt/augusto.esteves/EstevesCV-September2023.pdf)

All rights belong to the HCI Lab.

## Project Structure
- **MainActivity**
- **network.SocketRepository**: Deals with the network part of sending the user input through a UDP socket

App uses compose to manage UI. Main Activity uses a _style_ that desables back gesture that would interfere with user swipe.

## App Structure
- First page: Select IPV4 to send information to, click on **Connect** to move onto second page;
- Second page: Black page, send input information of coordinates from drag gestures through **SocketRepository**.
