const express = require('express')
const axios = require('axios');
const { response } = require('express');
const router = express.Router()
const base_url = require('./server.js').base_url;
const HEADERS = require('./server.js').headers;
const cors = require('cors');

// const base_url = 'https://api.yelp.com/v3/'
// const api_key = 'uQGYSvwIBI7fLrs1DQFuL85V1ZPNBiwSLSlsSucGmAe319_tqpUs-FeyRFGJ6WudJ_EFx-gWLKCvdYWONxicky4gm7K93LhK1GT6U3P5GZXXJZzCIEq593EZPQsdY3Yx'
// const HEADERS = {'Authorization': 'bearer ' + api_key}


// Autocomplete Route
router.get('/', cors(), (req, res) => {
    // res.send('Autocomplete!')
    
    // Payload
    let config = {
        headers:HEADERS,
        params:{
            'text': req.query.text
        }
    }

    let auto_list = []
    axios.get(base_url+`autocomplete`, config)
        .then(function (response) {
            response['data']['terms'].forEach(element => {
                auto_list.push(element);
            });
            response['data']['categories'].forEach(element=>{
                auto_list.push(
                    {
                        'text':element['title']
                    }
                );
            });
            console.log(response['data']['terms']);
            res.json({'status' : response.status, 'response' : auto_list});
        })
        .catch(function (error) {
            // #Send Error Status
            res.json({'status':error.response.status, 'response' : []});
        });
})

module.exports = router