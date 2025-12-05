#pragma once
#include <openssl/hmac.h>
#include <openssl/evp.h>
#include <string>
#include <iomanip>
#include <sstream>

std::string compute_hmac_sha256(const std::string& key, const std::string& data);