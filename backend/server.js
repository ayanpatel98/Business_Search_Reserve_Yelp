const express = require('express');
const app = express();
// const cors = require('cors');
const port = parseInt(process.env.PORT) || 8080;

// Destination API URL
const base_url = 'https://api.yelp.com/v3/'
const api_key = 'uQGYSvwIBI7fLrs1DQFuL85V1ZPNBiwSLSlsSucGmAe319_tqpUs-FeyRFGJ6WudJ_EFx-gWLKCvdYWONxicky4gm7K93LhK1GT6U3P5GZXXJZzCIEq593EZPQsdY3Yx'
const HEADERS = {'Authorization': 'bearer ' + api_key}
// Export the common elements
module.exports = {
    headers:HEADERS,
    base_url:base_url
}

// Define Routes
const autocomplete = require('./autocomplete')
const businesses_search = require('./businesses_search')
const businesses = require('./businesses')
const businesses_reviews = require('./business_reviews')

app.use('/autocomplete', autocomplete)
app.use('/businesses/search', businesses_search)
app.use('/businesses', businesses)
app.use('/businesses/reviews', businesses_reviews)

// Create Server
app.listen(port, () => {
    console.log(`Example app listening on port ${port}`)
})

