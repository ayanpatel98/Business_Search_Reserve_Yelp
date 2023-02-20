const express = require('express')
const axios = require('axios');
const router = express.Router()
const base_url = require('./server.js').base_url;
const HEADERS = require('./server.js').headers;
const cors = require('cors');


// Business Search Table
router.get('/', cors(), (req, res) => {
    
    // Payload
    let config = {
        headers:HEADERS,
        // params:{
        //     'term': 'ice',
        //     'latitude': 34.003,
        //     'longitude': -118.2863,
        //     'categories': 'food',
        //     'radius': 30000,
        // }
        params:{
            'term': req.query.term,
            'latitude': req.query.latitude,
            'longitude': req.query.longitude,
            'categories': req.query.categories,
            'radius': req.query.radius,
        }
    }
    console.log(config.params);

    business_obj = []

    axios.get(base_url+'businesses/search', config)
        .then(function (response) {
            let i=0;
            response.data.businesses.every(element => {
                if (i==10) return false
                i++;
                business_obj.push({
                    'id': element['id'],
                    'image_url': element['image_url'],
                    'name': element['name'],
                    'categories': element['categories'],
                    'rating': element['rating'],
                    'distance': element['distance']
                })
                return true
            });
            
            res.json({'status' : response.status, 'response' : business_obj});
        })
        .catch(function (error) {
            res.json({'status':error.response.status, 'response' : []});
        })
})

module.exports = router