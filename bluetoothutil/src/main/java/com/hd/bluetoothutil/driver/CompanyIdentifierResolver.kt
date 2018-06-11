package com.hd.bluetoothutil.driver

import android.util.SparseArray

/**
 * Created by hd on 2017/6/12 0008.
 * company
 * Reference resources ：
 * https://www.bluetooth.com/specifications/assigned-numbers/company-identifiers
 */
object CompanyIdentifierResolver {
    const val ERICSSON_TECHNOLOGY_LICENSING = 0x0000
    const val NOKIA_MOBILE_PHONES = 0x0001
    const val INTEL_CORP = 0x0002
    const val IBM_CORP = 0x0003
    const val TOSHIBA_CORP = 0x0004
    const val THREE_COM = 0x0005
    const val MICROSOFT = 0x0006
    const val LUCENT = 0x0007
    const val MOTOROLA = 0x0008
    const val INFINEON_TECHNOLOGIES_AG = 0x0009
    const val CAMBRIDGE_SILICON_RADIO = 0x000A
    const val SILICON_WAVE = 0x000B
    const val DIGIANSWER_A_S = 0x000C
    const val TEXAS_INSTRUMENTS_INC = 0x000D
    const val CEVA_INC_FORMERLY_PARTHUS_TECHNOLOGIES_INC = 0x000E
    const val BROADCOM_CORPORATION = 0x000F
    const val MITEL_SEMICONDUCTOR = 0x0010
    const val WIDCOMM_INC = 0x0011
    const val ZEEVO_INC = 0x0012
    const val ATMEL_CORPORATION = 0x0013
    const val MITSUBISHI_ELECTRIC_CORPORATION = 0x0014
    const val RTX_TELECOM_A_S = 0x0015
    const val KC_TECHNOLOGY_INC = 0x0016
    const val NEWLOGIC = 0x0017
    const val TRANSILICA_INC = 0x0018
    const val ROHDE_SCHWARZ_GMBH_CO_KG = 0x0019
    const val TTPCOM_LIMITED = 0x001A
    const val SIGNIA_TECHNOLOGIES_INC = 0x001B
    const val CONEXANT_SYSTEMS_INC = 0x001C
    const val QUALCOMM = 0x001D
    const val INVENTEL = 0x001E
    const val AVM_BERLIN = 0x001F
    const val BANDSPEED_INC = 0x0020
    const val MANSELLA_LTD = 0x0021
    const val NEC_CORPORATION = 0x0022
    const val WAVEPLUS_TECHNOLOGY_CO_LTD = 0x0023
    const val ALCATEL = 0x0024
    const val PHILIPS_SEMICONDUCTORS = 0x0025
    const val C_TECHNOLOGIES = 0x0026
    const val OPEN_INTERFACE = 0x0027
    const val R_F_MICRO_DEVICES = 0x0028
    const val HITACHI_LTD = 0x0029
    const val SYMBOL_TECHNOLOGIES_INC = 0x002A
    const val TENOVIS = 0x002B
    const val MACRONIX_INTERNATIONAL_CO_LTD = 0x002C
    const val GCT_SEMICONDUCTOR = 0x002D
    const val NORWOOD_SYSTEMS = 0x002E
    const val MEWTEL_TECHNOLOGY_INC = 0x002F
    const val ST_MICROELECTRONICS = 0x0030
    const val SYNOPSIS = 0x0031
    const val REDM_COMMUNICATIONS_LTD = 0x0032
    const val COMMIL_LTD = 0x0033
    const val COMPUTER_ACCESS_TECHNOLOGY_CORPORATION_CATC = 0x0034
    const val ECLIPSE_HQ_ESPANA_SL = 0x0035
    const val RENESAS_TECHNOLOGY_CORP = 0x0036
    const val MOBILIAN_CORPORATION = 0x0037
    const val TERAX = 0x0038
    const val INTEGRATED_SYSTEM_SOLUTION_CORP = 0x0039
    const val MATSUSHITA_ELECTRIC_INDUSTRIAL_CO_LTD = 0x003A
    const val GENNUM_CORPORATION = 0x003B
    const val RESEARCH_IN_MOTION = 0x003C
    const val IPEXTREME_INC = 0x003D
    const val SYSTEMS_AND_CHIPS_INC = 0x003E
    const val BLUETOOTH_SIG_INC = 0x003F
    const val SEIKO_EPSON_CORPORATION = 0x0040
    const val INTEGRATED_SILICON_SOLUTION_TAIWAN_INC = 0x0041
    const val CONWISE_TECHNOLOGY_CORPORATION_LTD = 0x0042
    const val PARROT_SA = 0x0043
    const val SOCKET_MOBILE = 0x0044
    const val ATHEROS_COMMUNICATIONS_INC = 0x0045
    const val MEDIATEK_INC = 0x0046
    const val BLUEGIGA = 0x0047
    const val MARVELL_TECHNOLOGY_GROUP_LTD = 0x0048
    const val THREE_DSP_CORPORATION = 0x0049
    const val ACCEL_SEMICONDUCTOR_LTD = 0x004A
    const val CONTINENTAL_AUTOMOTIVE_SYSTEMS = 0x004B
    const val APPLE_INC = 0x004C
    const val STACCATO_COMMUNICATIONS_INC = 0x004D
    const val AVAGO_TECHNOLOGIES = 0x004E
    const val APT_LICENSING_LTD = 0x004F
    const val SIRF_TECHNOLOGY = 0x0050
    const val TZERO_TECHNOLOGIES_INC = 0x0051
    const val JM_CORPORATION = 0x0052
    const val FREE2MOVE_AB = 0x0053
    const val THREE_DIJOY_CORPORATION = 0x0054
    const val PLANTRONICS_INC = 0x0055
    const val SONY_ERICSSON_MOBILE_COMMUNICATIONS = 0x0056
    const val HARMAN_INTERNATIONAL_INDUSTRIES_INC = 0x0057
    const val VIZIO_INC = 0x0058
    const val NORDIC_SEMICONDUCTOR_ASA = 0x0059
    const val EM_MICROELECTRONICMARIN_SA = 0x005A
    const val RALINK_TECHNOLOGY_CORPORATION = 0x005B
    const val BELKIN_INTERNATIONAL_INC = 0x005C
    const val REALTEK_SEMICONDUCTOR_CORPORATION = 0x005D
    const val STONESTREET_ONE_LLC = 0x005E
    const val WICENTRIC_INC = 0x005F
    const val RIVIERAWAVES_SAS = 0x0060
    const val RDA_MICROELECTRONICS = 0x0061
    const val GIBSON_GUITARS = 0x0062
    const val MICOMMAND_INC = 0x0063
    const val BAND_XI_INTERNATIONAL_LLC = 0x0064
    const val HEWLETTPACKARD_COMPANY = 0x0065
    const val NINE_SOLUTIONS_OY = 0x0066
    const val GN_NETCOM_A_S = 0x0067
    const val GENERAL_MOTORS = 0x0068
    const val AD_ENGINEERING_INC = 0x0069
    const val MINDTREE_LTD = 0x006A
    const val POLAR_ELECTRO_OY = 0x006B
    const val BEAUTIFUL_ENTERPRISE_CO_LTD = 0x006C
    const val BRIARTEK_INC = 0x006D
    const val SUMMIT_DATA_COMMUNICATIONS_INC = 0x006E
    const val SOUND_ID = 0x006F
    const val MONSTER_LLC = 0x0070
    const val CONNECTBLUE_AB = 0x0071
    const val SHANGHAI_SUPER_SMART_ELECTRONICS_CO_LTD = 0x0072
    const val GROUP_SENSE_LTD = 0x0073
    const val ZOMM_LLC = 0x0074
    const val SAMSUNG_ELECTRONICS_CO_LTD = 0x0075
    const val CREATIVE_TECHNOLOGY_LTD = 0x0076
    const val LAIRD_TECHNOLOGIES = 0x0077
    const val NIKE_INC = 0x0078
    const val LESSWIRE_AG = 0x0079
    const val MSTAR_SEMICONDUCTOR_INC = 0x007A
    const val HANLYNN_TECHNOLOGIES = 0x007B
    const val A_R_CAMBRIDGE = 0x007C
    const val SEERS_TECHNOLOGY_CO_LTD = 0x007D
    const val SPORTS_TRACKING_TECHNOLOGIES_LTD = 0x007E
    const val AUTONET_MOBILE = 0x007F
    const val DELORME_PUBLISHING_COMPANY_INC = 0x0080
    const val WUXI_VIMICRO = 0x0081
    const val SENNHEISER_COMMUNICATIONS_A_S = 0x0082
    const val TIMEKEEPING_SYSTEMS_INC = 0x0083
    const val LUDUS_HELSINKI_LTD = 0x0084
    const val BLUERADIOS_INC = 0x0085
    const val EQUINOX_AG = 0x0086
    const val GARMIN_INTERNATIONAL_INC = 0x0087
    const val ECOTEST = 0x0088
    const val GN_RESOUND_A_S = 0x0089
    const val JAWBONE = 0x008A
    const val TOPCORN_POSITIONING_SYSTEMS_LLC = 0x008B
    const val QUALCOMM_RETAIL_SOLUTIONS_INC_FORMERLY_QUALCOMM_LABS_INC = 0x008C
    const val ZSCAN_SOFTWARE = 0x008D
    const val QUINTIC_CORP = 0x008E
    const val STOLLMAN_EV_GMBH = 0x008F
    const val FUNAI_ELECTRIC_CO_LTD = 0x0090
    const val ADVANCED_PANMOBIL_SYSTEMS_GMBH_CO_KG = 0x0091
    const val THINKOPTICS_INC = 0x0092
    const val UNIVERSAL_ELECTRONICS_INC = 0x0093
    const val AIROHA_TECHNOLOGY_CORP = 0x0094
    const val NEC_LIGHTING_LTD = 0x0095
    const val ODM_TECHNOLOGY_INC = 0x0096
    const val CONNECTEDEVICE_LTD = 0x0097
    const val ZER01TV_GMBH = 0x0098
    const val ITECH_DYNAMIC_GLOBAL_DISTRIBUTION_LTD = 0x0099
    const val ALPWISE = 0x009A
    const val JIANGSU_TOPPOWER_AUTOMOTIVE_ELECTRONICS_CO_LTD = 0x009B
    const val COLORFY_INC = 0x009C
    const val GEOFORCE_INC = 0x009D
    const val BOSE_CORPORATION = 0x009E
    const val SUUNTO_OY = 0x009F
    const val KENSINGTON_COMPUTER_PRODUCTS_GROUP = 0x00A0
    const val SRMEDIZINELEKTRONIK = 0x00A1
    const val VERTU_CORPORATION_LIMITED = 0x00A2
    const val META_WATCH_LTD = 0x00A3
    const val LINAK_A_S = 0x00A4
    const val OTL_DYNAMICS_LLC = 0x00A5
    const val PANDA_OCEAN_INC = 0x00A6
    const val VISTEON_CORPORATION = 0x00A7
    const val ARP_DEVICES_LIMITED = 0x00A8
    const val MAGNETI_MARELLI_SPA = 0x00A9
    const val CAEN_RFID_SRL = 0x00AA
    const val INGENIEURSYSTEMGRUPPE_ZAHN_GMBH = 0x00AB
    const val GREEN_THROTTLE_GAMES = 0x00AC
    const val PETER_SYSTEMTECHNIK_GMBH = 0x00AD
    const val OMEGAWAVE_OY = 0x00AE
    const val CINETIX = 0x00AF
    const val PASSIF_SEMICONDUCTOR_CORP = 0x00B0
    const val SARIS_CYCLING_GROUP_INC = 0x00B1
    const val BEKEY_A_S = 0x00B2
    const val CLARINOX_TECHNOLOGIES_PTY_LTD = 0x00B3
    const val BDE_TECHNOLOGY_CO_LTD = 0x00B4
    const val SWIRL_NETWORKS = 0x00B5
    const val MESO_INTERNATIONAL = 0x00B6
    const val TRELAB_LTD = 0x00B7
    const val QUALCOMM_INNOVATION_CENTER_INC_QUIC = 0x00B8
    const val JOHNSON_CONTROLS_INC = 0x00B9
    const val STARKEY_LABORATORIES_INC = 0x00BA
    const val SPOWER_ELECTRONICS_LIMITED = 0x00BB
    const val ACE_SENSOR_INC = 0x00BC
    const val APLIX_CORPORATION = 0x00BD
    const val AAMP_OF_AMERICA = 0x00BE
    const val STALMART_TECHNOLOGY_LIMITED = 0x00BF
    const val AMICCOM_ELECTRONICS_CORPORATION = 0x00C0
    const val SHENZHEN_EXCELSECU_DATA_TECHNOLOGY_COLTD = 0x00C1
    const val GENEQ_INC = 0x00C2
    const val ADIDAS_AG = 0x00C3
    const val LG_ELECTRONICS = 0x00C4
    const val ONSET_COMPUTER_CORPORATION = 0x00C5
    const val SELFLY_BV = 0x00C6
    const val QUUPPA_OY = 0x00C7
    const val GELO_INC = 0x00C8
    const val EVLUMA = 0x00C9
    const val MC10 = 0x00CA
    const val BINAURIC_SE = 0x00CB
    const val BEATS_ELECTRONICS = 0x00CC
    const val MICROCHIP_TECHNOLOGY_INC = 0x00CD
    const val ELGATO_SYSTEMS_GMBH = 0x00CE
    const val ARCHOS_SA = 0x00CF
    const val DEXCOM_INC = 0x00D0
    const val POLAR_ELECTRO_EUROPE_BV = 0x00D1
    const val DIALOG_SEMICONDUCTOR_BV = 0x00D2
    const val TAIXINGBANG_TECHNOLOGY_HK_CO_LTD = 0x00D3
    const val KAWANTECH = 0x00D4
    const val AUSTCO_COMMUNICATION_SYSTEMS = 0x00D5
    const val TIMEX_GROUP_USA_INC = 0x00D6
    const val QUALCOMM_TECHNOLOGIES_INC = 0x00D7
    const val QUALCOMM_CONNECTED_EXPERIENCES_INC = 0x00D8
    const val VOYETRA_TURTLE_BEACH = 0x00D9
    const val TXTR_GMBH = 0x00DA
    const val BIOSENTRONICS = 0x00DB
    const val PROCTER_GAMBLE = 0x00DC
    const val HOSIDEN_CORPORATION = 0x00DD
    const val MUZIK_LLC = 0x00DE
    const val MISFIT_WEARABLES_CORP = 0x00DF
    const val GOOGLE = 0x00E0
    const val DANLERS_LTD = 0x00E1
    const val SEMILINK_INC = 0x00E2
    const val INMUSIC_BRANDS_INC = 0x00E3
    const val LS_RESEARCH_INC = 0x00E4
    const val EDEN_SOFTWARE_CONSULTANTS_LTD = 0x00E5
    const val FRESHTEMP = 0x00E6
    const val KS_TECHNOLOGIES = 0x00E7
    const val ACTS_TECHNOLOGIES = 0x00E8
    const val VTRACK_SYSTEMS = 0x00E9
    const val NIELSENKELLERMAN_COMPANY = 0x00EA
    const val SERVER_TECHNOLOGY_INC = 0x00EB
    const val BIORESEARCH_ASSOCIATES = 0x00EC
    const val JOLLY_LOGIC_LLC = 0x00ED
    const val ABOVE_AVERAGE_OUTCOMES_INC = 0x00EE
    const val BITSPLITTERS_GMBH = 0x00EF
    const val PAYPAL_INC = 0x00F0
    const val WITRON_TECHNOLOGY_LIMITED = 0x00F1
    const val MORSE_PROJECT_INC = 0x00F2
    const val KENT_DISPLAYS_INC = 0x00F3
    const val NAUTILUS_INC = 0x00F4
    const val SMARTIFIER_OY = 0x00F5
    const val ELCOMETER_LIMITED = 0x00F6
    const val VSN_TECHNOLOGIES_INC = 0x00F7
    const val ACEUNI_CORP_LTD = 0x00F8
    const val STICKNFIND = 0x00F9
    const val CRYSTAL_CODE_AB = 0x00FA
    const val KOUKAAM_AS = 0x00FB
    const val DELPHI_CORPORATION = 0x00FC
    const val VALENCETECH_LIMITED = 0x00FD
    const val RESERVED = 0x00FE
    const val TYPO_PRODUCTS_LLC = 0x00FF
    const val TOMTOM_INTERNATIONAL_BV = 0x0100
    const val FUGOO_INC = 0x0101
    const val KEISER_CORPORATION = 0x0102
    const val BANG_OLUFSEN_A_S = 0x0103
    const val PLUS_LOCATIONS_SYSTEMS_PTY_LTD = 0x0104
    const val UBIQUITOUS_COMPUTING_TECHNOLOGY_CORPORATION = 0x0105
    const val INNOVATIVE_YACHTTER_SOLUTIONS = 0x0106
    const val WILLIAM_DEMANT_HOLDING_A_S = 0x0107
    const val CHICONY_ELECTRONICS_CO_LTD = 0x0108
    const val ATUS_BV = 0x0109
    const val CODEGATE_LTD = 0x010A
    const val ERI_INC = 0x010B
    const val TRANSDUCERS_DIRECT_LLC = 0x010C
    const val FUJITSU_TEN_LIMITED = 0x010D
    const val AUDI_AG = 0x010E
    const val HISILICON_TECHNOLOGIES_CO_LTD = 0x010F
    const val NIPPON_SEIKI_CO_LTD = 0x0110
    const val STEELSERIES_APS = 0x0111
    const val VYZYBL_INC = 0x0112
    const val OPENBRAIN_TECHNOLOGIES_CO_LTD = 0x0113
    const val XENSR = 0x0114
    const val ESOLUTIONS = 0x0115
    const val ONE_OAK_TECHNOLOGIES = 0x0116
    const val WIMOTO_TECHNOLOGIES_INC = 0x0117
    const val RADIUS_NETWORKS_INC = 0x0118
    const val WIZE_TECHNOLOGY_CO_LTD = 0x0119
    const val QUALCOMM_LABS_INC = 0x011A
    const val ARUBA_NETWORKS = 0x011B
    const val BAIDU = 0x011C
    const val ARENDI_AG = 0x011D
    const val SKODA_AUTO_AS = 0x011E
    const val VOLKSWAGON_AG = 0x011F
    const val PORSCHE_AG = 0x0120
    const val SINO_WEALTH_ELECTRONIC_LTD = 0x0121
    const val AIRTURN_INC = 0x0122
    const val KINSA_INC = 0x0123
    const val HID_GLOBAL = 0x0124
    const val SEAT_ES = 0x0125
    const val PROMETHEAN_LTD = 0x0126
    const val SALUTICA_ALLIED_SOLUTIONS = 0x0127
    const val GPSI_GROUP_PTY_LTD = 0x0128
    const val NIMBLE_DEVICES_OY = 0x0129
    const val CHANGZHOU_YONGSE_INFOTECH_CO_LTD = 0x012A
    const val SPORTIQ = 0x012B
    const val TEMEC_INSTRUMENTS_BV = 0x012C
    const val SONY_CORPORATION = 0x012D
    const val ASSA_ABLOY = 0x012E
    const val CLARION_CO_LTD = 0x012F
    const val WAREHOUSE_INNOVATIONS = 0x0130
    const val CYPRESS_SEMICONDUCTOR_CORPORATION = 0x0131
    const val MADS_INC = 0x0132
    const val BLUE_MAESTRO_LIMITED = 0x0133
    const val RESOLUTION_PRODUCTS_INC = 0x0134
    const val AIREWEAR_LLC = 0x0135
    const val ETC_SP_ZOO = 0x0136
    const val PRESTIGIO_PLAZA_LTD = 0x0137

    private val COMPANY_NAME_MAP = populateCompanyNameMap()

    fun getCompanyName(companyId: Int, fallback: String): String {
        val name = COMPANY_NAME_MAP.get(companyId)
        return name ?: fallback
    }

    private fun populateCompanyNameMap(): SparseArray<String> {
        val map = SparseArray<String>()

        map.put(ERICSSON_TECHNOLOGY_LICENSING, "Ericsson Technology Licensing")
        map.put(NOKIA_MOBILE_PHONES, "Nokia Mobile Phones")
        map.put(INTEL_CORP, "Intel Corp.")
        map.put(IBM_CORP, "IBM Corp.")
        map.put(TOSHIBA_CORP, "Toshiba Corp.")
        map.put(THREE_COM, "3Com")
        map.put(MICROSOFT, "Microsoft")
        map.put(LUCENT, "Lucent")
        map.put(MOTOROLA, "Motorola")
        map.put(INFINEON_TECHNOLOGIES_AG, "Infineon Technologies AG")
        map.put(CAMBRIDGE_SILICON_RADIO, "Cambridge Silicon Radio")
        map.put(SILICON_WAVE, "Silicon Wave")
        map.put(DIGIANSWER_A_S, "Digianswer A/S")
        map.put(TEXAS_INSTRUMENTS_INC, "Texas Instruments Inc.")
        map.put(CEVA_INC_FORMERLY_PARTHUS_TECHNOLOGIES_INC, "Ceva, Inc. (formerly Parthus Technologies, Inc.)")
        map.put(BROADCOM_CORPORATION, "Broadcom Corporation")
        map.put(MITEL_SEMICONDUCTOR, "Mitel Semiconductor")
        map.put(WIDCOMM_INC, "Widcomm, Inc")
        map.put(ZEEVO_INC, "Zeevo, Inc.")
        map.put(ATMEL_CORPORATION, "Atmel Corporation")
        map.put(MITSUBISHI_ELECTRIC_CORPORATION, "Mitsubishi Electric Corporation")
        map.put(RTX_TELECOM_A_S, "RTX Telecom A/S")
        map.put(KC_TECHNOLOGY_INC, "KC Technology Inc.")
        map.put(NEWLOGIC, "NewLogic")
        map.put(TRANSILICA_INC, "Transilica, Inc.")
        map.put(ROHDE_SCHWARZ_GMBH_CO_KG, "Rohde & Schwarz GmbH & Co. KG")
        map.put(TTPCOM_LIMITED, "TTPCom Limited")
        map.put(SIGNIA_TECHNOLOGIES_INC, "Signia Technologies, Inc.")
        map.put(CONEXANT_SYSTEMS_INC, "Conexant Systems Inc.")
        map.put(QUALCOMM, "Qualcomm")
        map.put(INVENTEL, "Inventel")
        map.put(AVM_BERLIN, "AVM Berlin")
        map.put(BANDSPEED_INC, "BandSpeed, Inc.")
        map.put(MANSELLA_LTD, "Mansella Ltd")
        map.put(NEC_CORPORATION, "NEC Corporation")
        map.put(WAVEPLUS_TECHNOLOGY_CO_LTD, "WavePlus Technology Co., Ltd.")
        map.put(ALCATEL, "Alcatel")
        map.put(PHILIPS_SEMICONDUCTORS, "Philips Semiconductors")
        map.put(C_TECHNOLOGIES, "C Technologies")
        map.put(OPEN_INTERFACE, "Open Interface")
        map.put(R_F_MICRO_DEVICES, "R F Micro Devices")
        map.put(HITACHI_LTD, "Hitachi Ltd")
        map.put(SYMBOL_TECHNOLOGIES_INC, "Symbol Technologies, Inc.")
        map.put(TENOVIS, "Tenovis")
        map.put(MACRONIX_INTERNATIONAL_CO_LTD, "Macronix International Co. Ltd.")
        map.put(GCT_SEMICONDUCTOR, "GCT Semiconductor")
        map.put(NORWOOD_SYSTEMS, "Norwood Systems")
        map.put(MEWTEL_TECHNOLOGY_INC, "MewTel Technology Inc.")
        map.put(ST_MICROELECTRONICS, "ST Microelectronics")
        map.put(SYNOPSIS, "Synopsis")
        map.put(REDM_COMMUNICATIONS_LTD, "Red-M (Communications) Ltd")
        map.put(COMMIL_LTD, "Commil Ltd")
        map.put(COMPUTER_ACCESS_TECHNOLOGY_CORPORATION_CATC, "Computer Access Technology Corporation (CATC)")
        map.put(ECLIPSE_HQ_ESPANA_SL, "Eclipse (HQ Espana) S.L.")
        map.put(RENESAS_TECHNOLOGY_CORP, "Renesas Technology Corp.")
        map.put(MOBILIAN_CORPORATION, "Mobilian Corporation")
        map.put(TERAX, "Terax")
        map.put(INTEGRATED_SYSTEM_SOLUTION_CORP, "Integrated System Solution Corp.")
        map.put(MATSUSHITA_ELECTRIC_INDUSTRIAL_CO_LTD, "Matsushita Electric Industrial Co., Ltd.")
        map.put(GENNUM_CORPORATION, "Gennum Corporation")
        map.put(RESEARCH_IN_MOTION, "Research In Motion")
        map.put(IPEXTREME_INC, "IPextreme, Inc.")
        map.put(SYSTEMS_AND_CHIPS_INC, "Systems and Chips, Inc.")
        map.put(BLUETOOTH_SIG_INC, "Bluetooth SIG, Inc.")
        map.put(SEIKO_EPSON_CORPORATION, "Seiko Epson Corporation")
        map.put(INTEGRATED_SILICON_SOLUTION_TAIWAN_INC, "Integrated Silicon Solution Taiwan, Inc.")
        map.put(CONWISE_TECHNOLOGY_CORPORATION_LTD, "CONWISE Technology Corporation Ltd")
        map.put(PARROT_SA, "PARROT SA")
        map.put(SOCKET_MOBILE, "Socket Mobile")
        map.put(ATHEROS_COMMUNICATIONS_INC, "Atheros Communications, Inc.")
        map.put(MEDIATEK_INC, "MediaTek, Inc.")
        map.put(BLUEGIGA, "Bluegiga")
        map.put(MARVELL_TECHNOLOGY_GROUP_LTD, "Marvell Technology Group Ltd.")
        map.put(THREE_DSP_CORPORATION, "3DSP Corporation")
        map.put(ACCEL_SEMICONDUCTOR_LTD, "Accel Semiconductor Ltd.")
        map.put(CONTINENTAL_AUTOMOTIVE_SYSTEMS, "Continental Automotive Systems")
        map.put(APPLE_INC, "Apple, Inc.")
        map.put(STACCATO_COMMUNICATIONS_INC, "Staccato Communications, Inc.")
        map.put(AVAGO_TECHNOLOGIES, "Avago Technologies")
        map.put(APT_LICENSING_LTD, "APT Licensing Ltd.")
        map.put(SIRF_TECHNOLOGY, "SiRF Technology")
        map.put(TZERO_TECHNOLOGIES_INC, "Tzero Technologies, Inc.")
        map.put(JM_CORPORATION, "J&M Corporation")
        map.put(FREE2MOVE_AB, "Free2move AB")
        map.put(THREE_DIJOY_CORPORATION, "3DiJoy Corporation")
        map.put(PLANTRONICS_INC, "Plantronics, Inc.")
        map.put(SONY_ERICSSON_MOBILE_COMMUNICATIONS, "Sony Ericsson Mobile Communications")
        map.put(HARMAN_INTERNATIONAL_INDUSTRIES_INC, "Harman International Industries, Inc.")
        map.put(VIZIO_INC, "Vizio, Inc.")
        map.put(NORDIC_SEMICONDUCTOR_ASA, "Nordic Semiconductor ASA")
        map.put(EM_MICROELECTRONICMARIN_SA, "EM Microelectronic-Marin SA")
        map.put(RALINK_TECHNOLOGY_CORPORATION, "Ralink Technology Corporation")
        map.put(BELKIN_INTERNATIONAL_INC, "Belkin International, Inc.")
        map.put(REALTEK_SEMICONDUCTOR_CORPORATION, "Realtek Semiconductor Corporation")
        map.put(STONESTREET_ONE_LLC, "Stonestreet One, LLC")
        map.put(WICENTRIC_INC, "Wicentric, Inc.")
        map.put(RIVIERAWAVES_SAS, "RivieraWaves S.A.S")
        map.put(RDA_MICROELECTRONICS, "RDA Microelectronics")
        map.put(GIBSON_GUITARS, "Gibson Guitars")
        map.put(MICOMMAND_INC, "MiCommand Inc.")
        map.put(BAND_XI_INTERNATIONAL_LLC, "Band XI International, LLC")
        map.put(HEWLETTPACKARD_COMPANY, "Hewlett-Packard Company")
        map.put(NINE_SOLUTIONS_OY, "9Solutions Oy")
        map.put(GN_NETCOM_A_S, "GN Netcom A/S")
        map.put(GENERAL_MOTORS, "General Motors")
        map.put(AD_ENGINEERING_INC, "A&D Engineering, Inc.")
        map.put(MINDTREE_LTD, "MindTree Ltd.")
        map.put(POLAR_ELECTRO_OY, "Polar Electro OY")
        map.put(BEAUTIFUL_ENTERPRISE_CO_LTD, "Beautiful Enterprise Co., Ltd.")
        map.put(BRIARTEK_INC, "BriarTek, Inc.")
        map.put(SUMMIT_DATA_COMMUNICATIONS_INC, "Summit Data Communications, Inc.")
        map.put(SOUND_ID, "Sound ID")
        map.put(MONSTER_LLC, "Monster, LLC")
        map.put(CONNECTBLUE_AB, "connectBlue AB")
        map.put(SHANGHAI_SUPER_SMART_ELECTRONICS_CO_LTD, "ShangHai Super Smart Electronics Co. Ltd.")
        map.put(GROUP_SENSE_LTD, "Group Sense Ltd.")
        map.put(ZOMM_LLC, "Zomm, LLC")
        map.put(SAMSUNG_ELECTRONICS_CO_LTD, "Samsung Electronics Co. Ltd.")
        map.put(CREATIVE_TECHNOLOGY_LTD, "Creative Technology Ltd.")
        map.put(LAIRD_TECHNOLOGIES, "Laird Technologies")
        map.put(NIKE_INC, "Nike, Inc.")
        map.put(LESSWIRE_AG, "lesswire AG")
        map.put(MSTAR_SEMICONDUCTOR_INC, "MStar Semiconductor, Inc.")
        map.put(HANLYNN_TECHNOLOGIES, "Hanlynn Technologies")
        map.put(A_R_CAMBRIDGE, "A & R Cambridge")
        map.put(SEERS_TECHNOLOGY_CO_LTD, "Seers Technology Co. Ltd")
        map.put(SPORTS_TRACKING_TECHNOLOGIES_LTD, "Sports Tracking Technologies Ltd.")
        map.put(AUTONET_MOBILE, "Autonet Mobile")
        map.put(DELORME_PUBLISHING_COMPANY_INC, "DeLorme Publishing Company, Inc.")
        map.put(WUXI_VIMICRO, "WuXi Vimicro")
        map.put(SENNHEISER_COMMUNICATIONS_A_S, "Sennheiser Communications A/S")
        map.put(TIMEKEEPING_SYSTEMS_INC, "TimeKeeping Systems, Inc.")
        map.put(LUDUS_HELSINKI_LTD, "Ludus Helsinki Ltd.")
        map.put(BLUERADIOS_INC, "BlueRadios, Inc.")
        map.put(EQUINOX_AG, "equinox AG")
        map.put(GARMIN_INTERNATIONAL_INC, "Garmin International, Inc.")
        map.put(ECOTEST, "Ecotest")
        map.put(GN_RESOUND_A_S, "GN ReSound A/S")
        map.put(JAWBONE, "Jawbone")
        map.put(TOPCORN_POSITIONING_SYSTEMS_LLC, "Topcorn Positioning Systems, LLC")
        map.put(QUALCOMM_RETAIL_SOLUTIONS_INC_FORMERLY_QUALCOMM_LABS_INC, "Qualcomm Retail Solutions, Inc. (formerly Qualcomm Labs, Inc.)")
        map.put(ZSCAN_SOFTWARE, "Zscan Software")
        map.put(QUINTIC_CORP, "Quintic Corp.")
        map.put(STOLLMAN_EV_GMBH, "Stollman E+V GmbH")
        map.put(FUNAI_ELECTRIC_CO_LTD, "Funai Electric Co., Ltd.")
        map.put(ADVANCED_PANMOBIL_SYSTEMS_GMBH_CO_KG, "Advanced PANMOBIL Systems GmbH & Co. KG")
        map.put(THINKOPTICS_INC, "ThinkOptics, Inc.")
        map.put(UNIVERSAL_ELECTRONICS_INC, "Universal Electronics, Inc.")
        map.put(AIROHA_TECHNOLOGY_CORP, "Airoha Technology Corp.")
        map.put(NEC_LIGHTING_LTD, "NEC Lighting, Ltd.")
        map.put(ODM_TECHNOLOGY_INC, "ODM Technology, Inc.")
        map.put(CONNECTEDEVICE_LTD, "ConnecteDevice Ltd.")
        map.put(ZER01TV_GMBH, "zer01.tv GmbH")
        map.put(ITECH_DYNAMIC_GLOBAL_DISTRIBUTION_LTD, "i.Tech Dynamic Global Distribution Ltd.")
        map.put(ALPWISE, "Alpwise")
        map.put(JIANGSU_TOPPOWER_AUTOMOTIVE_ELECTRONICS_CO_LTD, "Jiangsu Toppower Automotive Electronics Co., Ltd.")
        map.put(COLORFY_INC, "Colorfy, Inc.")
        map.put(GEOFORCE_INC, "Geoforce Inc.")
        map.put(BOSE_CORPORATION, "Bose Corporation")
        map.put(SUUNTO_OY, "Suunto Oy")
        map.put(KENSINGTON_COMPUTER_PRODUCTS_GROUP, "Kensington Computer Products Group")
        map.put(SRMEDIZINELEKTRONIK, "SR-Medizinelektronik")
        map.put(VERTU_CORPORATION_LIMITED, "Vertu Corporation Limited")
        map.put(META_WATCH_LTD, "Meta Watch Ltd.")
        map.put(LINAK_A_S, "LINAK A/S")
        map.put(OTL_DYNAMICS_LLC, "OTL Dynamics LLC")
        map.put(PANDA_OCEAN_INC, "Panda Ocean Inc.")
        map.put(VISTEON_CORPORATION, "Visteon Corporation")
        map.put(ARP_DEVICES_LIMITED, "ARP Devices Limited")
        map.put(MAGNETI_MARELLI_SPA, "Magneti Marelli S.p.A")
        map.put(CAEN_RFID_SRL, "CAEN RFID srl")
        map.put(INGENIEURSYSTEMGRUPPE_ZAHN_GMBH, "Ingenieur-Systemgruppe Zahn GmbH")
        map.put(GREEN_THROTTLE_GAMES, "Green Throttle Games")
        map.put(PETER_SYSTEMTECHNIK_GMBH, "Peter Systemtechnik GmbH")
        map.put(OMEGAWAVE_OY, "Omegawave Oy")
        map.put(CINETIX, "Cinetix")
        map.put(PASSIF_SEMICONDUCTOR_CORP, "Passif Semiconductor Corp")
        map.put(SARIS_CYCLING_GROUP_INC, "Saris Cycling Group, Inc")
        map.put(BEKEY_A_S, "Bekey A/S")
        map.put(CLARINOX_TECHNOLOGIES_PTY_LTD, "Clarinox Technologies Pty. Ltd.")
        map.put(BDE_TECHNOLOGY_CO_LTD, "BDE Technology Co., Ltd.")
        map.put(SWIRL_NETWORKS, "Swirl Networks")
        map.put(MESO_INTERNATIONAL, "Meso international")
        map.put(TRELAB_LTD, "TreLab Ltd")
        map.put(QUALCOMM_INNOVATION_CENTER_INC_QUIC, "Qualcomm Innovation Center, Inc. (QuIC)")
        map.put(JOHNSON_CONTROLS_INC, "Johnson Controls, Inc.")
        map.put(STARKEY_LABORATORIES_INC, "Starkey Laboratories Inc.")
        map.put(SPOWER_ELECTRONICS_LIMITED, "S-Power Electronics Limited")
        map.put(ACE_SENSOR_INC, "Ace Sensor Inc")
        map.put(APLIX_CORPORATION, "Aplix Corporation")
        map.put(AAMP_OF_AMERICA, "AAMP of America")
        map.put(STALMART_TECHNOLOGY_LIMITED, "Stalmart Technology Limited")
        map.put(AMICCOM_ELECTRONICS_CORPORATION, "AMICCOM Electronics Corporation")
        map.put(SHENZHEN_EXCELSECU_DATA_TECHNOLOGY_COLTD, "Shenzhen Excelsecu Data Technology Co.,Ltd")
        map.put(GENEQ_INC, "Geneq Inc.")
        map.put(ADIDAS_AG, "adidas AG")
        map.put(LG_ELECTRONICS, "LG Electronics")
        map.put(ONSET_COMPUTER_CORPORATION, "Onset Computer Corporation")
        map.put(SELFLY_BV, "Selfly BV")
        map.put(QUUPPA_OY, "Quuppa Oy.")
        map.put(GELO_INC, "GeLo Inc")
        map.put(EVLUMA, "Evluma")
        map.put(MC10, "MC10")
        map.put(BINAURIC_SE, "Binauric SE")
        map.put(BEATS_ELECTRONICS, "Beats Electronics")
        map.put(MICROCHIP_TECHNOLOGY_INC, "Microchip Technology Inc.")
        map.put(ELGATO_SYSTEMS_GMBH, "Elgato Systems GmbH")
        map.put(ARCHOS_SA, "ARCHOS SA")
        map.put(DEXCOM_INC, "Dexcom, Inc.")
        map.put(POLAR_ELECTRO_EUROPE_BV, "Polar Electro Europe B.V.")
        map.put(DIALOG_SEMICONDUCTOR_BV, "Dialog Semiconductor B.V.")
        map.put(TAIXINGBANG_TECHNOLOGY_HK_CO_LTD, "Taixingbang Technology (HK) Co,. LTD.")
        map.put(KAWANTECH, "Kawantech")
        map.put(AUSTCO_COMMUNICATION_SYSTEMS, "Austco Communication Systems")
        map.put(TIMEX_GROUP_USA_INC, "Timex Group USA, Inc.")
        map.put(QUALCOMM_TECHNOLOGIES_INC, "Qualcomm Technologies, Inc.")
        map.put(QUALCOMM_CONNECTED_EXPERIENCES_INC, "Qualcomm Connected Experiences, Inc.")
        map.put(VOYETRA_TURTLE_BEACH, "Voyetra Turtle Beach")
        map.put(TXTR_GMBH, "txtr GmbH")
        map.put(BIOSENTRONICS, "Biosentronics")
        map.put(PROCTER_GAMBLE, "Procter & Gamble")
        map.put(HOSIDEN_CORPORATION, "Hosiden Corporation")
        map.put(MUZIK_LLC, "Muzik LLC")
        map.put(MISFIT_WEARABLES_CORP, "Misfit Wearables Corp")
        map.put(GOOGLE, "Google")
        map.put(DANLERS_LTD, "Danlers Ltd")
        map.put(SEMILINK_INC, "Semilink Inc")
        map.put(INMUSIC_BRANDS_INC, "inMusic Brands, Inc")
        map.put(LS_RESEARCH_INC, "L.S. Research Inc.")
        map.put(EDEN_SOFTWARE_CONSULTANTS_LTD, "Eden Software Consultants Ltd.")
        map.put(FRESHTEMP, "Freshtemp")
        map.put(KS_TECHNOLOGIES, "KS Technologies")
        map.put(ACTS_TECHNOLOGIES, "ACTS Technologies")
        map.put(VTRACK_SYSTEMS, "Vtrack Systems")
        map.put(NIELSENKELLERMAN_COMPANY, "Nielsen-Kellerman Company")
        map.put(SERVER_TECHNOLOGY_INC, "Server Technology, Inc.")
        map.put(BIORESEARCH_ASSOCIATES, "BioResearch Associates")
        map.put(JOLLY_LOGIC_LLC, "Jolly Logic, LLC")
        map.put(ABOVE_AVERAGE_OUTCOMES_INC, "Above Average Outcomes, Inc.")
        map.put(BITSPLITTERS_GMBH, "Bitsplitters GmbH")
        map.put(PAYPAL_INC, "PayPal, Inc.")
        map.put(WITRON_TECHNOLOGY_LIMITED, "Witron Technology Limited")
        map.put(MORSE_PROJECT_INC, "Morse Project Inc.")
        map.put(KENT_DISPLAYS_INC, "Kent Displays Inc.")
        map.put(NAUTILUS_INC, "Nautilus Inc.")
        map.put(SMARTIFIER_OY, "Smartifier Oy")
        map.put(ELCOMETER_LIMITED, "Elcometer Limited")
        map.put(VSN_TECHNOLOGIES_INC, "VSN Technologies Inc.")
        map.put(ACEUNI_CORP_LTD, "AceUni Corp., Ltd.")
        map.put(STICKNFIND, "StickNFind")
        map.put(CRYSTAL_CODE_AB, "Crystal Code AB")
        map.put(KOUKAAM_AS, "KOUKAAM a.s.")
        map.put(DELPHI_CORPORATION, "Delphi Corporation")
        map.put(VALENCETECH_LIMITED, "ValenceTech Limited")
        map.put(RESERVED, "Reserved")
        map.put(TYPO_PRODUCTS_LLC, "Typo Products, LLC")
        map.put(TOMTOM_INTERNATIONAL_BV, "TomTom International BV")
        map.put(FUGOO_INC, "Fugoo, Inc")
        map.put(KEISER_CORPORATION, "Keiser Corporation")
        map.put(BANG_OLUFSEN_A_S, "Bang & Olufsen A/S")
        map.put(PLUS_LOCATIONS_SYSTEMS_PTY_LTD, "PLUS Locations Systems Pty Ltd")
        map.put(UBIQUITOUS_COMPUTING_TECHNOLOGY_CORPORATION, "Ubiquitous Computing Technology Corporation")
        map.put(INNOVATIVE_YACHTTER_SOLUTIONS, "Innovative Yachtter Solutions")
        map.put(WILLIAM_DEMANT_HOLDING_A_S, "William Demant Holding A/S")
        map.put(CHICONY_ELECTRONICS_CO_LTD, "Chicony Electronics Co., Ltd.")
        map.put(ATUS_BV, "Atus BV")
        map.put(CODEGATE_LTD, "Codegate Ltd.")
        map.put(ERI_INC, "ERi, Inc.")
        map.put(TRANSDUCERS_DIRECT_LLC, "Transducers Direct, LLC")
        map.put(FUJITSU_TEN_LIMITED, "Fujitsu Ten Limited")
        map.put(AUDI_AG, "Audi AG")
        map.put(HISILICON_TECHNOLOGIES_CO_LTD, "HiSilicon Technologies Co., Ltd.")
        map.put(NIPPON_SEIKI_CO_LTD, "Nippon Seiki Co., Ltd.")
        map.put(STEELSERIES_APS, "Steelseries ApS")
        map.put(VYZYBL_INC, "vyzybl Inc.")
        map.put(OPENBRAIN_TECHNOLOGIES_CO_LTD, "Openbrain Technologies, Co., Ltd.")
        map.put(XENSR, "Xensr")
        map.put(ESOLUTIONS, "e.solutions")
        map.put(ONE_OAK_TECHNOLOGIES, "1OAK Technologies")
        map.put(WIMOTO_TECHNOLOGIES_INC, "Wimoto Technologies Inc")
        map.put(RADIUS_NETWORKS_INC, "Radius Networks, Inc.")
        map.put(WIZE_TECHNOLOGY_CO_LTD, "Wize Technology Co., Ltd.")
        map.put(QUALCOMM_LABS_INC, "Qualcomm Labs, Inc.")
        map.put(ARUBA_NETWORKS, "Aruba Networks")
        map.put(BAIDU, "Baidu")
        map.put(ARENDI_AG, "Arendi AG")
        map.put(SKODA_AUTO_AS, "Skoda Auto a.s.")
        map.put(VOLKSWAGON_AG, "Volkswagon AG")
        map.put(PORSCHE_AG, "Porsche AG")
        map.put(SINO_WEALTH_ELECTRONIC_LTD, "Sino Wealth Electronic Ltd.")
        map.put(AIRTURN_INC, "AirTurn, Inc.")
        map.put(KINSA_INC, "Kinsa, Inc.")
        map.put(HID_GLOBAL, "HID Global")
        map.put(SEAT_ES, "SEAT es")
        map.put(PROMETHEAN_LTD, "Promethean Ltd.")
        map.put(SALUTICA_ALLIED_SOLUTIONS, "Salutica Allied Solutions")
        map.put(GPSI_GROUP_PTY_LTD, "GPSI Group Pty Ltd")
        map.put(NIMBLE_DEVICES_OY, "Nimble Devices Oy")
        map.put(CHANGZHOU_YONGSE_INFOTECH_CO_LTD, "Changzhou Yongse Infotech Co., Ltd")
        map.put(SPORTIQ, "SportIQ")
        map.put(TEMEC_INSTRUMENTS_BV, "TEMEC Instruments B.V.")
        map.put(SONY_CORPORATION, "Sony Corporation")
        map.put(ASSA_ABLOY, "ASSA ABLOY")
        map.put(CLARION_CO_LTD, "Clarion Co., Ltd.")
        map.put(WAREHOUSE_INNOVATIONS, "Warehouse Innovations")
        map.put(CYPRESS_SEMICONDUCTOR_CORPORATION, "Cypress Semiconductor Corporation")
        map.put(MADS_INC, "MADS Inc")
        map.put(BLUE_MAESTRO_LIMITED, "Blue Maestro Limited")
        map.put(RESOLUTION_PRODUCTS_INC, "Resolution Products, Inc.")
        map.put(AIREWEAR_LLC, "Airewear LLC")
        map.put(ETC_SP_ZOO, "ETC sp. z.o.o.")
        map.put(PRESTIGIO_PLAZA_LTD, "Prestigio Plaza Ltd.")
        return map
    }
}
