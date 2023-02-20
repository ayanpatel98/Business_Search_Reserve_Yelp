const express = require('express')
const axios = require('axios');
const router = express.Router()
const base_url = require('./server.js').base_url;
const HEADERS = require('./server.js').headers;
const cors = require('cors');

// Business Search Details
router.get('/', cors(), (req, res) => {
    // Payload
    let config = {
        headers:HEADERS
    }
    let b_id = String(req.query.b_id);

    let business_details_data = []
    axios.get(base_url+`businesses/`+b_id, config)
        .then(function (response) {
            // console.log(response.data);
            business_details_data.push(
                {
                    'id': response.data['id'],
                    'name': response.data['name'],
                    'is_closed': response.data['is_closed'],
                    'is_open_now': ('hours' in response.data) ? response.data['hours'][0]['is_open_now'] : 'noStatus',
                    'transactions': response.data['transactions'],
                    'categories': response.data['categories'],
                    'display_address': response.data['location']['display_address'],
                    'display_phone': response.data['display_phone'],
                    'price': ('price' in response.data) ? response.data['price'] : '',
                    'more_info': response.data['url'],
                    'photos': response.data['photos'],
                    'coordinates': response.data['coordinates'],
                }
            );
            res.json({'status' : response.status, 'response' : business_details_data});
        })
        .catch(function (error) {
            console.log(error);
            res.json({'status':error.response.status, 'response' : []});
        })
})

module.exports = router