import React from 'react';
import ReactDOM from 'react-dom';

import Jumbotron from 'react-bootstrap/lib/Jumbotron';

import UserInterface from 'cas10/Layout/UserInterface';
import UserInterfaceStore from 'cas10/Layout/UserInterfaceStore';

var uiStore = new UserInterfaceStore();

ReactDOM.render(
    <UserInterface brand="PGMan" uiStore={uiStore}>
        <Jumbotron>Testando</Jumbotron>
    </UserInterface>,
    document.getElementById('react-app')
)