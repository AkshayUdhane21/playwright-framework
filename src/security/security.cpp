#include "security/security.hpp"

std::string compute_hmac_sha256(const std::string& key, const std::string& data) {
    unsigned char hash[EVP_MAX_MD_SIZE];
    unsigned int hashLen = 0;

    HMAC(EVP_sha256(),
        data.c_str(), static_cast<int>(data.size()),
        reinterpret_cast<const unsigned char*>(key.c_str()), key.size(),
        hash, &hashLen);

    std::ostringstream oss;
    oss << std::hex << std::setfill('0');
    for (unsigned int i = 0; i < hashLen; ++i)
        oss << std::setw(2) << static_cast<unsigned>(hash[i]);
    return oss.str();
}