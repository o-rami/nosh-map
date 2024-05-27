import './body.css';
import React from 'react';
import Top from './Top/Top';
import FavoriteList from './FavoriteListing/FavoriteList';
import Map from './GoogleMap/Map';


export default function Body() {

    return (
        <div className='mainContent ml-5'>
            <Top />

            <FavoriteList />
        </div>


    )
}