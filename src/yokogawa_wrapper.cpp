#include <iostream>                    // std::cout
#include <string>                      // std::string
#include <msclr/marshal_cppstd.h>      // marshal_as
#include <vcclr.h>                     // gcroot, but not needed here
#include "application/application.hpp"

using namespace System;
using namespace msclr::interop;

namespace HeartbeatService {
	public ref class Yokogawa {
	private:
		Application* app;
	public:
		Yokogawa(String^ cs_cfg_path) { std::string cfg_path = marshal_as<std::string>(cs_cfg_path); app = new Application(cfg_path); }
		~Yokogawa() { this->!Yokogawa(); }
		!Yokogawa() { if (app != nullptr) { delete app; app = nullptr; } }

		void run() { app->run(); }
		void stop() { app->stop(); }
	};
}