package com.nikoarap.slotmachine.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nikoarap.slotmachine.R
import com.nikoarap.slotmachine.db.classes.User
import com.nikoarap.slotmachine.other.Constants

import com.nikoarap.slotmachine.other.Constants.KEY_NEW_USER

import com.nikoarap.slotmachine.ui.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var user: User

    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firstConfig = sharedPreferences.getBoolean(Constants.KEY_TOGGLE_FIRST_TIME, true)
        if (firstConfig) {
            findNavController().navigate(
                R.id.action_registerFragment_to_dashboardFragment,
                savedInstanceState
            )
        }
        val countries = arrayOf(
            "Afghanistan",
            "Albania",
            "Algeria",
            "American Samoa",
            "Andorra",
            "Angola",
            "Anguilla",
            "Antarctica",
            "Antigua and Barbuda",
            "Argentina",
            "Armenia",
            "Aruba",
            "Australia",
            "Austria",
            "Azerbaijan",
            "Bahamas",
            "Bahrain",
            "Bangladesh",
            "Barbados",
            "Belarus",
            "Belgium",
            "Belize",
            "Benin",
            "Bermuda",
            "Bhutan",
            "Bolivia",
            "Bosnia and Herzegowina",
            "Botswana",
            "Bouvet Island",
            "Brazil",
            "British Indian Ocean Territory",
            "Brunei Darussalam",
            "Bulgaria",
            "Burkina Faso",
            "Burundi",
            "Cambodia",
            "Cameroon",
            "Canada",
            "Cape Verde",
            "Cayman Islands",
            "Central African Republic",
            "Chad",
            "Chile",
            "China",
            "Christmas Island",
            "Cocos (Keeling) Islands",
            "Colombia",
            "Comoros",
            "Congo",
            "Congo, the Democratic Republic of the",
            "Cook Islands",
            "Costa Rica",
            "Cote d'Ivoire",
            "Croatia (Hrvatska)",
            "Cuba",
            "Cyprus",
            "Czech Republic",
            "Denmark",
            "Djibouti",
            "Dominica",
            "Dominican Republic",
            "East Timor",
            "Ecuador",
            "Egypt",
            "El Salvador",
            "Equatorial Guinea",
            "Eritrea",
            "Estonia",
            "Ethiopia",
            "Falkland Islands (Malvinas)",
            "Faroe Islands",
            "Fiji",
            "Finland",
            "France",
            "France Metropolitan",
            "French Guiana",
            "French Polynesia",
            "French Southern Territories",
            "Gabon",
            "Gambia",
            "Georgia",
            "Germany",
            "Ghana",
            "Gibraltar",
            "Greece",
            "Greenland",
            "Grenada",
            "Guadeloupe",
            "Guam",
            "Guatemala",
            "Guinea",
            "Guinea-Bissau",
            "Guyana",
            "Haiti",
            "Heard and Mc Donald Islands",
            "Holy See (Vatican City State)",
            "Honduras",
            "Hong Kong",
            "Hungary",
            "Iceland",
            "India",
            "Indonesia",
            "Iran (Islamic Republic of)",
            "Iraq",
            "Ireland",
            "Israel",
            "Italy",
            "Jamaica",
            "Japan",
            "Jordan",
            "Kazakhstan",
            "Kenya",
            "Kiribati",
            "Korea, Democratic People's Republic of",
            "Korea, Republic of",
            "Kuwait",
            "Kyrgyzstan",
            "Lao, People's Democratic Republic",
            "Latvia",
            "Lebanon",
            "Lesotho",
            "Liberia",
            "Libyan Arab Jamahiriya",
            "Liechtenstein",
            "Lithuania",
            "Luxembourg",
            "Macau",
            "Maroc",
            "Macedonia, The Former Yugoslav Republic of",
            "Madagascar",
            "Malawi",
            "Malaysia",
            "Maldives",
            "Mali",
            "Malta",
            "Marshall Islands",
            "Martinique",
            "Mauritania",
            "Mauritius",
            "Mayotte",
            "Mexico",
            "Micronesia, Federated States of",
            "Moldova, Republic of",
            "Monaco",
            "Mongolia",
            "Montserrat",
            "Morocco",
            "Mozambique",
            "Myanmar",
            "Namibia",
            "Nauru",
            "Nepal",
            "Netherlands",
            "Netherlands Antilles",
            "New Caledonia",
            "New Zealand",
            "Nicaragua",
            "Niger",
            "Nigeria",
            "Niue",
            "Norfolk Island",
            "Northern Mariana Islands",
            "Norway",
            "Oman",
            "Pakistan",
            "Palau",
            "Panama",
            "Papua New Guinea",
            "Paraguay",
            "Peru",
            "Philippines",
            "Pitcairn",
            "Poland",
            "Portugal",
            "Puerto Rico",
            "Qatar",
            "Reunion",
            "Romania",
            "Russian Federation",
            "Rwanda",
            "Saint Kitts and Nevis",
            "Saint Lucia",
            "Saint Vincent and the Grenadines",
            "Samoa",
            "San Marino",
            "Sao Tome and Principe",
            "Saudi Arabia",
            "Senegal",
            "Seychelles",
            "Sierra Leone",
            "Singapore",
            "Slovakia (Slovak Republic)",
            "Slovenia",
            "Solomon Islands",
            "Somalia",
            "South Africa",
            "South Georgia and the South Sandwich Islands",
            "Spain",
            "Sri Lanka",
            "St. Helena",
            "St. Pierre and Miquelon",
            "Sudan",
            "Suriname",
            "Svalbard and Jan Mayen Islands",
            "Swaziland",
            "Sweden",
            "Switzerland",
            "Syrian Arab Republic",
            "Taiwan, Province of China",
            "Tajikistan",
            "Tanzania, United Republic of",
            "Thailand",
            "Togo",
            "Tokelau",
            "Tonga",
            "Trinidad and Tobago",
            "Tunisia",
            "Turkey",
            "Turkmenistan",
            "Turks and Caicos Islands",
            "Tuvalu",
            "Uganda",
            "Ukraine",
            "United Arab Emirates",
            "United Kingdom",
            "United States",
            "United States Minor Outlying Islands",
            "Uruguay",
            "Uzbekistan",
            "Vanuatu",
            "Venezuela",
            "Vietnam",
            "Virgin Islands (British)",
            "Virgin Islands (U.S.)",
            "Wallis and Futuna Islands",
            "Western Sahara",
            "Yemen",
            "Yugoslavia",
            "Zambia",
            "Zimbabwe",
            "Palestine"
        )
        val countryAreaCodes = arrayOf(
            "93", "355", "213",
            "376", "244", "672", "54", "374", "297", "61", "43", "994", "973",
            "880", "375", "32", "501", "229", "975", "591", "387", "267", "55",
            "673", "359", "226", "95", "257", "855", "237", "1", "238", "236",
            "235", "56", "86", "61", "61", "57", "269", "242", "682", "506",
            "385", "53", "357", "420", "45", "253", "670", "593", "20", "503",
            "240", "291", "372", "251", "500", "298", "679", "358", "33",
            "689", "241", "220", "995", "49", "233", "350", "30", "299", "502",
            "224", "245", "592", "509", "504", "852", "36", "91", "62", "98",
            "964", "353", "44", "972", "39", "225", "1876", "81", "962", "7",
            "254", "686", "965", "996", "856", "371", "961", "266", "231",
            "218", "423", "370", "352", "853", "389", "261", "265", "60",
            "960", "223", "356", "692", "222", "230", "262", "52", "691",
            "373", "377", "976", "382", "212", "258", "264", "674", "977",
            "31", "687", "64", "505", "227", "234", "683", "850", "47", "968",
            "92", "680", "507", "675", "595", "51", "63", "870", "48", "351",
            "1", "974", "40", "7", "250", "590", "685", "378", "239", "966",
            "221", "381", "248", "232", "65", "421", "386", "677", "252", "27",
            "82", "34", "94", "290", "508", "249", "597", "268", "46", "41",
            "963", "886", "992", "255", "66", "228", "690", "676", "216", "90",
            "993", "688", "971", "256", "44", "380", "598", "1", "998", "678",
            "39", "58", "84", "681", "967", "260", "263"
        )

        val codeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countryAreaCodes)
        val countryAdapter =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, countries)

        tv_birthPlace.setAdapter(countryAdapter)
        tv_birthPlace.threshold = 1
        tv_paysDeResidence.setAdapter(countryAdapter)
        tv_paysDeResidence.threshold = 1
        tv_codePays.setAdapter(codeAdapter)
        tv_codePays.threshold = 1


        tv_birthDate.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val ddmmyyyy = "DDMMYYYY"
            private val cal: Calendar = Calendar.getInstance()
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    var clean = s.toString().replace("[^\\d.]".toRegex(), "")
                    val cleanC = current.replace("[^\\d.]".toRegex(), "")
                    val cl = clean.length
                    var sel = cl
                    var i = 2
                    while (i <= cl && i < 6) {
                        sel++
                        i += 2
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean == cleanC) sel--
                    if (clean.length < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length)
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        var day = clean.substring(0, 2).toInt()
                        var mon = clean.substring(2, 4).toInt()
                        var year = clean.substring(4, 8).toInt()
                        if (mon > 12) mon = 12
                        cal.set(Calendar.MONTH, mon - 1)
                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                        cal.set(Calendar.YEAR, year)
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012
                        day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(
                            Calendar.DATE
                        ) else day
                        clean = String.format("%02d%02d%02d", day, mon, year)
                    }
                    clean = String.format(
                        "%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8)
                    )
                    sel = if (sel < 0) 0 else sel
                    current = clean
                    tv_birthDate.setText(current)
                    tv_birthDate.setSelection(if (sel < current.length) sel else current.length)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
        tv_email.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val adressPattern: Pattern = Pattern
                    .compile(
                        "[a-zA-Z0-9+._%-+]{1,256}" + "@"
                                + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                                + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+"
                    )
                if (!adressPattern.matcher(s).matches()) {
                    textInputLayout3.error = "valid email is required"
                    textInputLayout3.isErrorEnabled = true
                } else {
                    textInputLayout3.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })



        btn_goDashboard.setOnClickListener {
            findNavController().navigate(
                R.id.action_registerFragment_to_dashboardFragment,
                savedInstanceState
            )
        }


        btn_saveUser.setOnClickListener {

            if (tv_firstName.text.toString() == "" || tv_lastName.text.toString() == "" ||
                tv_email.text.toString() == "" || textInputLayout3.isErrorEnabled ||
                tv_phone.text.toString() == "" || tv_codePays.text.toString() == ""
                || tv_birthDate.text.toString() == "" || tv_birthPlace.text.toString() == ""
                || tv_ville.text.toString() == "" || tv_paysDeResidence.text.toString() == ""
                || tv_codePostale.text.toString() == ""
            ) {
                Snackbar.make(requireView(), Constants.ENTER_ALL_VALUES, Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                val userFirstName = tv_firstName.text.toString()
                val userLastName = tv_lastName.text.toString()
                val userEmail = tv_email.text.toString()
                val userPhone = tv_codePays.text.toString() + " " + tv_phone.text.toString()
                val userBirthDate = tv_birthDate.text.toString()
                val userBirthPlace = tv_birthPlace.text.toString()
                val userVille = tv_ville.text.toString()
                val userCodePostale = tv_codePostale.text.toString()
                val userResdiencePlace = tv_paysDeResidence.text.toString()
                var acceptedPlans = "Not Accepted"

                if (cb_parEmail.isChecked) {
                    acceptedPlans = "Par e-mail"
                } else if (cb_parSMS.isChecked) {
                    acceptedPlans = "Par SMS"
                }
                if (cb_parEmail.isChecked && cb_parSMS.isChecked) {
                    acceptedPlans ="Par e-mail et Par SMS"
                }
                user = User(
                    userFirstName,
                    userLastName,
                    userEmail,
                    userPhone,
                    userBirthDate,
                    userBirthPlace,
                    userVille,
                    userCodePostale,
                    userResdiencePlace,
                    acceptedPlans
                )

                saveUser(user)

            }


        }

        tv_visitez.setOnClickListener {
            findNavController().navigate(
                R.id.action_registerFragment_to_legalFragment,
                savedInstanceState
            )
        }


    }

    private fun saveUser(user: User) {
        viewModel.insertUser(user)
        Snackbar.make(
            requireView(),
            Constants.USER_SAVED_SUCCESSFULLY,
            Snackbar.LENGTH_SHORT
        ).show()

        findNavController().navigate(
            R.id.action_registerFragment_to_jackpotFragment
        )
        sharedPreferences.edit()
            .putBoolean(KEY_NEW_USER, false)
            .apply()
    }
}


